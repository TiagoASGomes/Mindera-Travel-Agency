package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.StatusNameAlreadyExistsException;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;

import java.util.List;

public interface PaymentStatusService {

    List<PaymentStatusGetDto> getAll();

    PaymentStatusGetDto getById(Long id) throws PaymentStatusNotFoundException;

    PaymentStatusGetDto getByName(String name) throws PaymentStatusNotFoundException;

    PaymentStatusGetDto create(PaymentStatusCreateDto paymentStatus) throws StatusNameAlreadyExistsException;

    List<PaymentStatusGetDto> createList(List<PaymentStatusCreateDto> paymentStatusList) throws StatusNameAlreadyExistsException;

    PaymentStatusGetDto update(Long id, PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException, StatusNameAlreadyExistsException;

    void delete(Long id) throws PaymentStatusNotFoundException, PaymentStatusInUseException;

    PaymentStatus findById(Long id) throws PaymentStatusNotFoundException;

    PaymentStatus findByName(String name) throws PaymentStatusNotFoundException;
}
