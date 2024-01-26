package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;

import java.util.List;

public interface InvoiceService {
    Invoice findById(Long id) throws InvoiceNotFoundException;

    List<InvoiceGetDto> getAll();

    InvoiceGetDto getById(Long id) throws InvoiceNotFoundException;

    List<InvoiceGetDto> getByUserId(Long id);

    InvoiceGetDto create(InvoiceCreateDto invoice) throws UserNotFoundException;

    void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException;

    InvoiceGetDto update(Long id, InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException;

    void updatePrice(Long id, int price) throws InvoiceNotFoundException;
}
