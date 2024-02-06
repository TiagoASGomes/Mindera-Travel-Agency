package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class FlightTicketConverter {
    public List<TicketGetDto> fromEntityListToGetDtoList(Collection<FlightTicket> flightTickets) {
        if (flightTickets == null) return new ArrayList<>();
        return flightTickets.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public TicketGetDto fromEntityToGetDto(FlightTicket flightTicket) {
        return new TicketGetDto(
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

    public FlightTicket fromCreateDtoToEntity(TicketCreateDto flightTicket, Invoice invoice) {

        return FlightTicket.builder()
                .fName(flightTicket.fName())
                .email(flightTicket.email())
                .phone(flightTicket.phone())
                .price(flightTicket.price())
                .maxLuggageWeight(flightTicket.maxLuggageWeight())
                .carryOnLuggage(flightTicket.carryOnLuggage())
                .invoice(invoice)
                .priceId(flightTicket.priceId())
                .flightId(flightTicket.flightId())
                .build();
    }

    public List<ExternalFlightCreateDto> fromEntityListToExternalDtoList(Set<FlightTicket> flightTickets) {
        if (flightTickets == null) return new ArrayList<>();
        return flightTickets.stream()
                .map(this::fromEntityToExternalDto)
                .toList();
    }

    private ExternalFlightCreateDto fromEntityToExternalDto(FlightTicket flightTicket) {
        return new ExternalFlightCreateDto(
                flightTicket.getFName(),
                flightTicket.getEmail(),
                flightTicket.getPhone(),
                flightTicket.getFlightId(),
                flightTicket.getPriceId()
        );
    }
}
