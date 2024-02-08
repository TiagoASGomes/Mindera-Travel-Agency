package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalRoomInfoDto(
        @Schema(description = "Room number", example = "1")
        int roomNumber,
        @Schema(description = "Number of beds", example = "2")
        int numberOfBeds,
        @Schema(description = "Room type", example = "SINGLEROOM")
        String roomType,
        @Schema(description = "Room price per night", example = "100")
        int roomPrice
) implements Serializable {
}
