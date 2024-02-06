package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record TicketUpdateTicketDto(
        @NotNull(message = INVALID_PRICE)
        @Min(value = 0, message = INVALID_PRICE)
        Integer price,
        @NotNull(message = INVALID_LUGGAGE_WEIGHT)
        @Min(value = 0, message = INVALID_LUGGAGE_WEIGHT)
        @Max(value = 30, message = INVALID_LUGGAGE_WEIGHT)
        Integer maxLuggageWeight,
        @NotNull(message = INVALID_CARRY_ON_LUGGAGE)
        Boolean carryOnLuggage
) {
}
