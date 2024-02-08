package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalFlightInfoDto(
        @Schema(description = "The flight's id", example = "1")
        Long id,
        @Schema(description = "The flight's origin", example = "LIS")
        String origin,
        @Schema(description = "The flight's destination", example = "OPO")
        String destination,
        @Schema(description = "The flight's duration in hours", example = "1.5")
        int duration,
        @Schema(description = "The flight's date of departure", example = "2021-12-31T12:00")
        LocalDateTime dateOfFlight,
        @Schema(description = "The flight's available seats", example = "100")
        int availableSeats,
        @Schema(description = "The flight's plane dto")
        ExternalPlaneInfoDto plane,
        @Schema(description = "The flight's prices dtos")
        List<ExternalPriceInfoDto> price

) implements Serializable {
}
