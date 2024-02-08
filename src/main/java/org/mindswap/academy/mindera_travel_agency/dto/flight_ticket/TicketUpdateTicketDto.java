package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record TicketUpdateTicketDto(
        @NotNull(message = INVALID_PRICE)
        @Min(value = 0, message = INVALID_PRICE)
        @Schema(description = "The price of the ticket", example = "100")
        Integer price,
        @NotNull(message = INVALID_FARE_CLASS)
        @Pattern(regexp = "^[A-Z]+$", message = INVALID_FARE_CLASS)
        @Schema(description = "The fare class of the ticket", example = "ECONOMY")
        String fareClass,
        @NotNull(message = INVALID_LUGGAGE_WEIGHT)
        @Min(value = 0, message = INVALID_LUGGAGE_WEIGHT)
        @Max(value = 30, message = INVALID_LUGGAGE_WEIGHT)
        @Schema(description = "The maximum luggage weight the user can bring", example = "20")
        Integer maxLuggageWeight,
        @NotNull(message = INVALID_CARRY_ON_LUGGAGE)
        @Schema(description = "If the user has carry-on luggage", example = "true")
        Boolean carryOnLuggage
) {
}
