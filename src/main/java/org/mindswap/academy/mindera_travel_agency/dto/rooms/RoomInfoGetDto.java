package org.mindswap.academy.mindera_travel_agency.dto.rooms;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record RoomInfoGetDto(
        @Schema(description = "The room's id", example = "1")
        Long id,
        @Schema(description = "The room's type", example = "SINGLEROOM")
        String roomType,
        @Schema(description = "The room's number", example = "1")
        int roomNumber,
        @Schema(description = "The number of beds", example = "1")
        int numberOfBeds,
        @Schema(description = "The price per night", example = "100")
        int pricePerNight
) implements Serializable {
}
