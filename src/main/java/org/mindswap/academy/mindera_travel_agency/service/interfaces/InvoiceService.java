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

/**
 * The InvoiceService interface provides methods to manage invoices in the travel agency system.
 */

public interface InvoiceService {
    /**
     * Retrieves all invoices with pagination.
     *
     * @param page the pagination information
     * @return a page of InvoiceGetDto objects
     */
    Page<InvoiceGetDto> getAll(Pageable page);

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the ID of the invoice
     * @return the InvoiceGetDto object representing the invoice
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    InvoiceGetDto getById(Long id) throws InvoiceNotFoundException;

    /**
     * Creates a new invoice.
     *
     * @param invoice the InvoiceCreateDto object representing the invoice to be created
     * @return the InvoiceGetDto object representing the created invoice
     * @throws UserNotFoundException          if the user is not found
     * @throws PaymentStatusNotFoundException if the payment status is not found
     */
    InvoiceGetDto create(InvoiceCreateDto invoice) throws UserNotFoundException, PaymentStatusNotFoundException;

    /**
     * Updates an existing invoice.
     *
     * @param id      the ID of the invoice to be updated
     * @param invoice the InvoiceUpdateDto object representing the updated invoice
     * @return the InvoiceGetDto object representing the updated invoice
     * @throws InvoiceNotFoundException       if the invoice is not found
     * @throws PaymentStatusNotFoundException if the payment status is not found
     * @throws PaymentCompletedException      if the payment is already completed
     */
    InvoiceGetDto update(Long id, InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException;

    /**
     * Finalizes an invoice by completing the payment process.
     *
     * @param id the ID of the invoice to be finalized
     * @return the InvoiceGetDto object representing the finalized invoice
     * @throws InvoiceNotFoundException       if the invoice is not found
     * @throws PaymentCompletedException      if the payment is already completed
     * @throws InvoiceNotCompleteException    if the invoice is not complete
     * @throws PaymentStatusNotFoundException if the payment status is not found
     * @throws UnirestException               if an error occurs during the payment process
     * @throws JsonProcessingException        if an error occurs while processing JSON data
     */
    InvoiceGetDto finalizeInvoice(Long id) throws InvoiceNotFoundException, PaymentCompletedException, InvoiceNotCompleteException, PaymentStatusNotFoundException, UnirestException, JsonProcessingException;

    /**
     * Deletes an invoice.
     *
     * @param id the ID of the invoice to be deleted
     * @throws InvoiceNotFoundException  if the invoice is not found
     * @throws PaymentCompletedException if the payment is already completed
     */
    void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException;

    /**
     * Finds an invoice by its ID.
     *
     * @param id the ID of the invoice
     * @return the Invoice object representing the invoice
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    Invoice findById(Long id) throws InvoiceNotFoundException;

    /**
     * Updates the price of the hotel in an invoice.
     *
     * @param id the ID of the invoice
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    void updateHotelPrice(Long id, int hotelPrice) throws InvoiceNotFoundException;

    /**
     * Updates the prices of flight tickets in an invoice.
     *
     * @param invoiceFlights the list of FlightTicket objects representing the flights in the invoice
     * @param id             the ID of the invoice
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    void updateFlightPrices(List<FlightTicket> invoiceFlights, Long id) throws InvoiceNotFoundException;
}
