package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;

import java.time.LocalDateTime;
import java.util.List;

public record InvoiceGetDto(
        Long id,
        int totalPrice,
        LocalDateTime paymentDate,
        String paymentStatus,
        HotelReservationGetDto hotelReservation,
        List<FlightTicketGetDto> flightTickets
) {
}
