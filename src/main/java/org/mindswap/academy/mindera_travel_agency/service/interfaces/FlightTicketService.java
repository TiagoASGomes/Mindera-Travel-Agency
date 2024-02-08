package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdatePersonalInfo;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdateTicketDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.MaxFlightPerInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This interface represents the Flight Ticket Service, which provides methods for managing flight tickets.
 */
public interface FlightTicketService {
    /**
     * Retrieves all flight tickets with pagination.
     *
     * @param page the pagination information
     * @return a page of flight ticket DTOs
     */
    Page<TicketGetDto> getAll(Pageable page);

    /**
     * Retrieves all flight tickets associated with a specific invoice.
     *
     * @param invoiceId the ID of the invoice
     * @return a list of flight ticket DTOs
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    List<TicketGetDto> getAllByInvoice(Long invoiceId) throws InvoiceNotFoundException;

    /**
     * Retrieves a flight ticket by its ID.
     *
     * @param id the ID of the flight ticket
     * @return the flight ticket DTO
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     */
    TicketGetDto getById(Long id) throws FlightTicketNotFoundException;

    /**
     * Creates a new flight ticket.
     *
     * @param flightTicket the flight ticket create DTO
     * @return the created flight ticket DTO
     * @throws InvoiceNotFoundException     if the associated invoice is not found
     * @throws MaxFlightPerInvoiceException if the maximum number of flights per invoice is reached
     * @throws PaymentCompletedException    if the associated invoice is already completed
     */
    TicketGetDto create(TicketCreateDto flightTicket) throws InvoiceNotFoundException, MaxFlightPerInvoiceException, PaymentCompletedException;

    /**
     * Updates the total price of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the updated flight ticket create DTO
     * @return the updated flight ticket DTO
     * @throws InvoiceNotFoundException      if the associated invoice is not found
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the associated invoice is already completed
     */
    TicketGetDto updateTotal(Long id, TicketCreateDto flightTicket) throws InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException;

    /**
     * Updates the personal information of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the updated flight ticket personal info DTO
     * @return the updated flight ticket DTO
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the associated invoice is already completed
     */
    TicketGetDto updatePersonalInfo(Long id, TicketUpdatePersonalInfo flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException;

    /**
     * Updates the ticket information of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the updated flight ticket info DTO
     * @return the updated flight ticket DTO
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the associated invoice is already completed
     * @throws InvoiceNotFoundException      if the associated invoice is not found
     */
    TicketGetDto updateTicketInfo(Long id, TicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    /**
     * Deletes a flight ticket by its ID.
     *
     * @param id the ID of the flight ticket
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the associated invoice is already completed
     */
    void delete(Long id) throws FlightTicketNotFoundException, PaymentCompletedException;

    /**
     * Finds a flight ticket by its ID.
     *
     * @param id the ID of the flight ticket
     * @return the flight ticket entity
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     */
    FlightTicket findById(Long id) throws FlightTicketNotFoundException;
}
