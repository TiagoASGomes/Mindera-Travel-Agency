package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlightTicketConverter {
    public List<FlightTicketGetDto> fromEntityListToGetDtoList(List<FlightTicket> flightTickets) {
        return null;
    }

    public FlightTicketGetDto fromEntityToGetDto(FlightTicket flightTicket) {
        return null;
    }

    public FlightTicket fromCreateDtoToEntity(FlightTicketCreateDto flightTicket) {
        return null;
    }
}
