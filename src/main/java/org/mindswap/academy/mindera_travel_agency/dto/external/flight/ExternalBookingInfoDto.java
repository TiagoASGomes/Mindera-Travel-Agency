package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalBookingInfoDto(
        @Schema(description = "The booking id", example = "1")
        Long id,
        @Schema(description = "The User full name", example = "John Doe")
        String fName,
        @Schema(description = "The User email", example = "email@example.com")
        String email,
        @Schema(description = "The User phone", example = "912345678")
        String phone,
        @Schema(description = "The User seat number", example = "1A")
        String seatNumber,
        @Schema(description = "Flight info dto")
        ExternalFlightInfoDto flight,
        @Schema(description = "Price info dto")
        ExternalPriceInfoDto price
) {
}
