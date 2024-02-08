package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPriceInfoDto(
        @Schema(description = "The id of the price", example = "1")
        Long id,
        @Schema(description = "The price of the flight", example = "100")
        int price,
        @Schema(description = "The class name of the flight", example = "Economy")
        String className
) implements Serializable {
}
