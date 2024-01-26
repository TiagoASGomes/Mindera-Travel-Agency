package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;

import java.util.List;

public interface FlightTicketService {
    List<FlightTicketGetDto> getAll();

    FlightTicketGetDto getById(Long id) throws FlightTicketNotFoundException;

    FlightTicketGetDto create(FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException;

    FlightTicketGetDto update(Long id, FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException;

    FlightTicketGetDto updatePartial(Long id, FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException, FlightTicketDuplicateException;

    void delete(Long id) throws FlightTicketNotFoundException;

    List<FlightTicketGetDto> getAllByUser(String sortBy, Long userId);

    List<FlightTicketGetDto> getAllByInvoice(String sortBy, Long invoiceId);
}
