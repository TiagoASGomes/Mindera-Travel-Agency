package org.mindswap.academy.mindera_travel_agency.dto.fare_class;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_FARE_CLASS;

public record FareClassCreateDto(
        @NotNull(message = INVALID_FARE_CLASS)
        @Pattern(regexp = "^[a-z]+$", message = INVALID_FARE_CLASS)
        String className
) {
}
