package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InvoiceService {
    Invoice findById(Long id) throws InvoiceNotFoundException;

    Page<InvoiceGetDto> getAll(Pageable page);

    InvoiceGetDto getById(Long id) throws InvoiceNotFoundException;

    InvoiceGetDto create(InvoiceCreateDto invoice) throws UserNotFoundException, PaymentStatusNotFoundException;

    void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException;

    InvoiceGetDto update(Long id, InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException;

    void updatePrice(Long id) throws InvoiceNotFoundException;

    InvoiceGetDto finalize(Long id) throws InvoiceNotFoundException, PaymentCompletedException, InvoiceNotCompleteException, PaymentStatusNotFoundException, UnirestException, JsonProcessingException;
}
