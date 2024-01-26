package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record FlightTicketUpdateDto(
        //TODO regex
        @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = INVALID_NAME)
        String fName,
        @Pattern(regexp = "", message = INVALID_EMAIL)
        String email,
        @Pattern(regexp = "", message = INVALID_PHONE_NUMBER)
        String phone,
        @Min(value = 0, message = INVALID_PRICE)
        int price,
        @Pattern(regexp = "^[a-z]+$", message = INVALID_FARE_CLASS)
        String fareClass,
        @Min(value = 0, message = INVALID_LUGGAGE_WEIGHT)
        @Max(value = 25, message = INVALID_LUGGAGE_WEIGHT)
        int maxLuggageWeight,
        boolean carryOnLuggage
) {
}
