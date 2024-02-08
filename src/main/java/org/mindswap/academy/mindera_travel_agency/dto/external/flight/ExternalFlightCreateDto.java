package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExternalFlightCreateDto(
        @Schema(description = "The User full name", example = "John Doe")
        String fName,
        @Schema(description = "The User email", example = "email@example.com")
        String email,
        @Schema(description = "The User phone", example = "912345678")
        String phone,
        @Schema(description = "The flight id", example = "1")
        Long flightId,
        @Schema(description = "The price id", example = "1")
        Long priceId
) {
}
