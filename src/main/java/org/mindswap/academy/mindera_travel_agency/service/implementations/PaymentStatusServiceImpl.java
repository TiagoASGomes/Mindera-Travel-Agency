package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.PaymentStatusConverter;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.mindswap.academy.mindera_travel_agency.repository.PaymentStatusRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.NAME_NOT_FOUND;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private PaymentStatusConverter paymentStatusConverter;

    @Override
    public PaymentStatus findByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findByName(name).orElseThrow(() -> new PaymentStatusNotFoundException(NAME_NOT_FOUND + name));
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
    public PaymentStatusGetDto getByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusConverter.fromEntityToGetDto(findByName(name));
    }

    @Override
    public PaymentStatusGetDto create(PaymentStatusCreateDto paymentStatus) {
        PaymentStatus paymentStatusToSave = paymentStatusConverter.fromCreateDtoToEntity(paymentStatus);
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToSave));
    }

    @Override
    public PaymentStatusGetDto update(Long id, PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException {
        PaymentStatus paymentStatusToUpdate = findById(id);
        paymentStatusToUpdate.setStatusName(paymentStatus.statusName());
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToUpdate));
    }

    @Override
    public void delete(Long id) throws PaymentStatusNotFoundException {
        findById(id);
        paymentStatusRepository.deleteById(id);
    }

    public PaymentStatus findById(Long id) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findById(id).orElseThrow(() -> new PaymentStatusNotFoundException(ID_NOT_FOUND + id));
    }
}
