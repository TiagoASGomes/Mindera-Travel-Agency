package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;

import java.util.Set;

public record InvoiceGetDto(
        Long id,
        int totalPrice,
        String paymentDate,
        String paymentStatus,
        HotelReservationGetDto hotelReservation,
        Set<FlightTicketGetDto> flightTickets
) {
}
