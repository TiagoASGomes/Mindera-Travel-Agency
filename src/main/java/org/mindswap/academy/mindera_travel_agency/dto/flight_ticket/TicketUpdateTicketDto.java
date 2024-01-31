package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record TicketUpdateTicketDto(
        @NotNull(message = INVALID_TICKET_NUMBER)
        @Min(value = 1, message = INVALID_TICKET_NUMBER)
        Long ticketNumber,
        @NotNull(message = INVALID_SEAT_NUMBER)
        @Pattern(regexp = "\\d[A-Z]", message = INVALID_SEAT_NUMBER)
        String seatNumber,
        @NotNull(message = INVALID_PRICE)
        @Min(value = 0, message = INVALID_PRICE)
        Integer price,
        @NotNull(message = INVALID_FARE_CLASS)
        @Pattern(regexp = "^[a-z]+$", message = INVALID_FARE_CLASS)
        String fareClass,
        @NotNull(message = INVALID_LUGGAGE_WEIGHT)
        @Min(value = 0, message = INVALID_LUGGAGE_WEIGHT)
        @Max(value = 30, message = INVALID_LUGGAGE_WEIGHT)
        Integer maxLuggageWeight,
        @NotNull(message = INVALID_CARRY_ON_LUGGAGE)
        Boolean carryOnLuggage
) {
}