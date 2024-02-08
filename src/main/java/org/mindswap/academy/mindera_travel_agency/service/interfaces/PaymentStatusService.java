package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.StatusNameAlreadyExistsException;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;

import java.util.List;

/**
 * The PaymentStatusService interface provides methods to manage payment status entities.
 */
public interface PaymentStatusService {
    /**
     * Retrieves all payment status entities.
     *
     * @return a list of PaymentStatusGetDto objects representing the payment status entities.
     */
    List<PaymentStatusGetDto> getAll();

    /**
     * Retrieves a payment status entity by its ID.
     *
     * @param id the ID of the payment status entity.
     * @return a PaymentStatusGetDto object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     */
    PaymentStatusGetDto getById(Long id) throws PaymentStatusNotFoundException;

    /**
     * Retrieves a payment status entity by its name.
     *
     * @param name the name of the payment status entity.
     * @return a PaymentStatusGetDto object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified name is not found.
     */
    PaymentStatusGetDto getByName(String name) throws PaymentStatusNotFoundException;

    /**
     * Creates a new payment status entity.
     *
     * @param paymentStatus the PaymentStatusCreateDto object containing the data for the new payment status entity.
     * @return a PaymentStatusGetDto object representing the created payment status entity.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    PaymentStatusGetDto create(PaymentStatusCreateDto paymentStatus) throws StatusNameAlreadyExistsException;

    /**
     * Creates multiple payment status entities.
     *
     * @param paymentStatusList a list of PaymentStatusCreateDto objects containing the data for the new payment status entities.
     * @return a list of PaymentStatusGetDto objects representing the created payment status entities.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    List<PaymentStatusGetDto> createList(List<PaymentStatusCreateDto> paymentStatusList) throws StatusNameAlreadyExistsException;

    /**
     * Updates a payment status entity.
     *
     * @param id            the ID of the payment status entity to be updated.
     * @param paymentStatus the PaymentStatusCreateDto object containing the updated data for the payment status entity.
     * @return a PaymentStatusGetDto object representing the updated payment status entity.
     * @throws PaymentStatusNotFoundException   if the payment status entity with the specified ID is not found.
     * @throws StatusNameAlreadyExistsException if a payment status entity with the same name already exists.
     */
    PaymentStatusGetDto update(Long id, PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException, StatusNameAlreadyExistsException;

    /**
     * Deletes a payment status entity.
     *
     * @param id the ID of the payment status entity to be deleted.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     * @throws PaymentStatusInUseException    if the payment status entity is currently in use and cannot be deleted.
     */
    void delete(Long id) throws PaymentStatusNotFoundException, PaymentStatusInUseException;

    /**
     * Finds a payment status entity by its ID.
     *
     * @param id the ID of the payment status entity.
     * @return a PaymentStatus object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified ID is not found.
     */
    PaymentStatus findById(Long id) throws PaymentStatusNotFoundException;

    /**
     * Finds a payment status entity by its name.
     *
     * @param name the name of the payment status entity.
     * @return a PaymentStatus object representing the payment status entity.
     * @throws PaymentStatusNotFoundException if the payment status entity with the specified name is not found.
     */
    PaymentStatus findByName(String name) throws PaymentStatusNotFoundException;
}
