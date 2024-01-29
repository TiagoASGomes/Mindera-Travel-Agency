package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record FlightTicketUpdateDto(
        //TODO mexer nos parametros obrigatorios
        @NotNull(message = INVALID_NAME)
        @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = INVALID_NAME)
        String fName,
        @NotNull(message = INVALID_EMAIL)
        @Pattern(regexp = "", message = INVALID_EMAIL)
        String email,
        @NotNull(message = INVALID_PHONE_NUMBER)
        @Pattern(regexp = "", message = INVALID_PHONE_NUMBER)
        String phone,
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
