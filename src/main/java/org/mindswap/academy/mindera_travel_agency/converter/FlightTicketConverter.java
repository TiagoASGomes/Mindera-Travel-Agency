package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class FlightTicketConverter {
    public List<FlightTicketGetDto> fromEntityListToGetDtoList(Collection<FlightTicket> flightTickets) {
        if (flightTickets == null) return new ArrayList<>();
        return flightTickets.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public FlightTicketGetDto fromEntityToGetDto(FlightTicket flightTicket) {
        return new FlightTicketGetDto(
                flightTicket.getId(),
                flightTicket.getFName(),
                flightTicket.getEmail(),
                flightTicket.getPhone(),
                flightTicket.getTicketNumber(),
                flightTicket.getSeatNumber(),
                flightTicket.getPrice(),
                flightTicket.getMaxLuggageWeight(),
                flightTicket.isCarryOnLuggage()
        );
    }

    public FlightTicket fromCreateDtoToEntity(FlightTicketCreateDto flightTicket, FareClass fareClass, Invoice invoice) {

        return FlightTicket.builder()
                .fName(flightTicket.fName())
                .email(flightTicket.email())
                .phone(flightTicket.phone())
                .ticketNumber(flightTicket.ticketNumber())
                .seatNumber(flightTicket.seatNumber())
                .price(flightTicket.price())
                .maxLuggageWeight(flightTicket.maxLuggageWeight())
                .carryOnLuggage(flightTicket.carryOnLuggage())
                .fareClass(fareClass)
                .invoice(invoice)
                .build();
    }
}
