package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPlaneInfoDto(
        @Schema(description = "The plane id", example = "1")
        Long id,
        @Schema(description = "The planes maximum seating capacity", example = "200")
        int peopleCapacity,
        @Schema(description = "The planes maximum luggage capacity in kg", example = "2000")
        int luggageCapacity,
        @Schema(description = "The planes company owner", example = "Ryanair")
        String companyOwner,
        @Schema(description = "The planes model name", example = "Boeing 737")
        String modelName
) implements Serializable {
}
