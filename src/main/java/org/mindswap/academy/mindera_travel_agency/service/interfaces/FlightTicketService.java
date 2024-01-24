package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;

import java.util.List;

public interface FlightTicketService {
    List<FlightTicketGetDto> getAll();

    FlightTicketGetDto getById(Long id) throws FlightTicketNotFoundException;

    FlightTicketGetDto create(FlightTicketCreateDto flightTicket);

    FlightTicketGetDto update(Long id, FlightTicketCreateDto flightTicket);

    FlightTicketGetDto updatePartial(Long id, FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException;

    void delete(Long id) throws FlightTicketNotFoundException;
}
