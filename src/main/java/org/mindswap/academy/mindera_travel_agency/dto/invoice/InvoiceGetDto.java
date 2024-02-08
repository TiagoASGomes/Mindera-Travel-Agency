package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceGetDto(
        @Schema(description = "The invoice's id", example = "1")
        Long id,
        @Schema(description = "The total price of the invoice", example = "1000")
        int totalPrice,
        @Schema(description = "The date of the payment", example = "2022-12-31T23:59:59")
        LocalDateTime paymentDate,
        @Schema(description = "The payment status", example = "PAID")
        String paymentStatus,
        @Schema(description = "The hotel reservation")
        HotelReservationGetDto hotelReservation,
        @Schema(description = "The flight tickets")
        List<TicketGetDto> flightTickets
) implements Serializable {
}
