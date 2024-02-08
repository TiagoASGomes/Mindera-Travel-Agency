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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

/**
 * This class implements the PaymentStatusService interface and provides the implementation for managing payment status entities.
 */
@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentStatusConverter paymentStatusConverter;

    @Autowired
    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository, PaymentStatusConverter paymentStatusConverter) {
        this.paymentStatusRepository = paymentStatusRepository;
        this.paymentStatusConverter = paymentStatusConverter;
    }

    /**
     * Retrieves all payment status entities.
     *
     * @return a list of PaymentStatusGetDto objects representing the payment status entities.
     */
    @Override
    public List<PaymentStatusGetDto> getAll() {
        return paymentStatusConverter.fromEntityListToGetDtoList(paymentStatusRepository.findAll());
    }

    /**
     * Retrieves a payment status entity by its ID.
     *
     * @param id the ID of the payment status entity.
     * @return a PaymentStatusGetDto object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     */
    @Override
    public PaymentStatusGetDto getById(Long id) throws PaymentStatusNotFoundException {
        return paymentStatusConverter.fromEntityToGetDto(findById(id));
    }

    /**
     * Retrieves a payment status entity by its name.
     *
     * @param name the name of the payment status entity.
     * @return a PaymentStatusGetDto object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified name is not found.
     */
    @Override
    public PaymentStatusGetDto getByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusConverter.fromEntityToGetDto(findByName(name));
    }

    /**
     * Creates a new payment status entity.
     *
     * @param paymentStatus the PaymentStatusCreateDto object representing the payment status to be created.
     * @return a PaymentStatusGetDto object representing the created payment status entity.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    @Override
    public PaymentStatusGetDto create(PaymentStatusCreateDto paymentStatus) throws StatusNameAlreadyExistsException {
        Optional<PaymentStatus> duplicate = paymentStatusRepository.findByStatusName(paymentStatus.statusName());
        if (duplicate.isPresent()) {
            throw new StatusNameAlreadyExistsException(PAYMENT_STATUS_DUPLICATE);
        }
        PaymentStatus paymentStatusToSave = paymentStatusConverter.fromCreateDtoToEntity(paymentStatus);
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToSave));
    }

    /**
     * Creates multiple payment status entities.
     *
     * @param paymentStatusList a list of PaymentStatusCreateDto objects representing the payment statuses to be created.
     * @return a list of PaymentStatusGetDto objects representing the created payment status entities.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    @Override
    public List<PaymentStatusGetDto> createList(List<PaymentStatusCreateDto> paymentStatusList) throws StatusNameAlreadyExistsException {
        List<PaymentStatusGetDto> result = new ArrayList<>();
        for (PaymentStatusCreateDto paymentStatus : paymentStatusList) {
            result.add(create(paymentStatus));
        }
        return result;
    }

    /**
     * Updates a payment status entity.
     *
     * @param id            the ID of the payment status entity to be updated.
     * @param paymentStatus the PaymentStatusCreateDto object representing the updated payment status.
     * @return a PaymentStatusGetDto object representing the updated payment status entity.
     * @throws PaymentStatusNotFoundException   if the payment status entity with the specified ID is not found.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    @Override
    public PaymentStatusGetDto update(Long id, PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException, StatusNameAlreadyExistsException {
        Optional<PaymentStatus> duplicate = paymentStatusRepository.findByStatusName(paymentStatus.statusName());
        if (duplicate.isPresent()) {
            throw new StatusNameAlreadyExistsException(PAYMENT_STATUS_DUPLICATE);
        }
        PaymentStatus paymentStatusToUpdate = findById(id);
        paymentStatusToUpdate.setStatusName(paymentStatus.statusName());
        return paymentStatusConverter.fromEntityToGetDto(paymentStatusRepository.save(paymentStatusToUpdate));
    }

    /**
     * Deletes a payment status entity.
     *
     * @param id the ID of the payment status entity to be deleted.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     * @throws PaymentStatusInUseException    if the payment status entity is in use by invoices.
     */
    @Override
    public void delete(Long id) throws PaymentStatusNotFoundException, PaymentStatusInUseException {
        PaymentStatus status = findById(id);
        if (status.getInvoices() != null && !status.getInvoices().isEmpty()) {
            throw new PaymentStatusInUseException(PAYMENT_STATUS_IN_USE);
        }
        paymentStatusRepository.deleteById(id);
    }

    /**
     * Finds a payment status entity by its name.
     *
     * @param name the name of the payment status entity.
     * @return a PaymentStatus object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified name is not found.
     */
    @Override
    public PaymentStatus findByName(String name) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findByStatusName(name).orElseThrow(() -> new PaymentStatusNotFoundException(STATUS_NAME_NOT_FOUND + name));
    }

    /**
     * Finds a payment status entity by its ID.
     *
     * @param id the ID of the payment status entity.
     * @return a PaymentStatus object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     */
    @Override
    public PaymentStatus findById(Long id) throws PaymentStatusNotFoundException {
        return paymentStatusRepository.findById(id).orElseThrow(() -> new PaymentStatusNotFoundException(STATUS_ID_NOT_FOUND + id));
    }
}
