package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.PaymentStatusConverter;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.StatusNameAlreadyExistsException;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.mindswap.academy.mindera_travel_agency.repository.PaymentStatusRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentStatusConverter paymentStatusConverter;

    @Autowired
    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository, PaymentStatusConverter paymentStatusConverter) {
        this.paymentStatusRepository = paymentStatusRepository;
        this.paymentStatusConverter = paymentStatusConverter;
    }

    @Override
    public List<PaymentStatusGetDto> getAll() {
        return paymentStatusConverter.fromEntityListToGetDtoList(paymentStatusRepository.findAll());
    }

    @Override
    public PaymentStatusGetDto getById(Long id) throws PaymentStatusNotFoundException {
        return paymentStatusConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    @Cacheable(value = "paymentStatus", key = "#name")
    public PaymentStatusGetDto getByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusConverter.fromEntityToGetDto(findByName(name));
    }

    @Override
    public PaymentStatusGetDto create(PaymentStatusCreateDto paymentStatus) throws StatusNameAlreadyExistsException {
        Optional<PaymentStatus> duplicate = paymentStatusRepository.findByStatusName(paymentStatus.statusName());
        if (duplicate.isPresent()) {
            throw new StatusNameAlreadyExistsException(PAYMENT_STATUS_DUPLICATE);
        }
        PaymentStatus paymentStatusToSave = paymentStatusConverter.fromCreateDtoToEntity(paymentStatus);
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToSave));
    }

    @Override
    public List<PaymentStatusGetDto> createList(List<PaymentStatusCreateDto> paymentStatusList) throws StatusNameAlreadyExistsException {
        List<PaymentStatusGetDto> result = new ArrayList<>();
        for (PaymentStatusCreateDto paymentStatus : paymentStatusList) {
            result.add(create(paymentStatus));
        }
        return result;
    }

    @Override
    @CachePut(value = "paymentStatus", key = "#id")
    public PaymentStatusGetDto update(Long id, PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException, StatusNameAlreadyExistsException {
        Optional<PaymentStatus> duplicate = paymentStatusRepository.findByStatusName(paymentStatus.statusName());
        if (duplicate.isPresent()) {
            throw new StatusNameAlreadyExistsException(PAYMENT_STATUS_DUPLICATE);
        }
        PaymentStatus paymentStatusToUpdate = findById(id);
        paymentStatusToUpdate.setStatusName(paymentStatus.statusName());
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToUpdate));
    }

    @Override
    public void delete(Long id) throws PaymentStatusNotFoundException, PaymentStatusInUseException {
        PaymentStatus status = findById(id);
        if (status.getInvoices() != null && !status.getInvoices().isEmpty()) {
            throw new PaymentStatusInUseException(PAYMENT_STATUS_IN_USE);
        }
        paymentStatusRepository.deleteById(id);
    }

    @Override
    public PaymentStatus findByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findByStatusName(name).orElseThrow(() -> new PaymentStatusNotFoundException(STATUS_NAME_NOT_FOUND + name));
    }

    @Override
    public PaymentStatus findById(Long id) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findById(id).orElseThrow(() -> new PaymentStatusNotFoundException(STATUS_ID_NOT_FOUND + id));
    }
}
