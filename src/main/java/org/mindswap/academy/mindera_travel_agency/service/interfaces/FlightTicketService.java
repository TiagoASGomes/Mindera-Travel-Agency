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

public interface FlightTicketService {
    Page<TicketGetDto> getAll(Pageable page);

    List<TicketGetDto> getAllByInvoice(Long invoiceId) throws InvoiceNotFoundException;

    TicketGetDto getById(Long id) throws FlightTicketNotFoundException;

    TicketGetDto create(TicketCreateDto flightTicket) throws InvoiceNotFoundException, MaxFlightPerInvoiceException, PaymentCompletedException;

    TicketGetDto updateTotal(Long id, TicketCreateDto flightTicket) throws InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException;

    TicketGetDto updatePersonalInfo(Long id, TicketUpdatePersonalInfo flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException;

    TicketGetDto updateTicketInfo(Long id, TicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    void delete(Long id) throws FlightTicketNotFoundException, PaymentCompletedException;

    FlightTicket findById(Long id) throws FlightTicketNotFoundException;
}
