package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.user.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface InvoiceService {
    Page<InvoiceGetDto> getAll(Pageable page);

    InvoiceGetDto getById(Long id) throws InvoiceNotFoundException;

    InvoiceGetDto create(InvoiceCreateDto invoice) throws UserNotFoundException, PaymentStatusNotFoundException;

    InvoiceGetDto update(Long id, InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException;

    InvoiceGetDto finalizeInvoice(Long id) throws InvoiceNotFoundException, PaymentCompletedException, InvoiceNotCompleteException, PaymentStatusNotFoundException, UnirestException, JsonProcessingException;

    void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException;

    Invoice findById(Long id) throws InvoiceNotFoundException;

    void updateHotelPrice(Long id) throws InvoiceNotFoundException;

    void updateFlightPrices(List<FlightTicket> invoiceFlights, Long id) throws InvoiceNotFoundException;
}
