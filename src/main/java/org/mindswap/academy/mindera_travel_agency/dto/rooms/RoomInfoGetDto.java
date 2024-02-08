package org.mindswap.academy.mindera_travel_agency.dto.rooms;

import java.io.Serializable;

public record RoomInfoGetDto(
        Long id,
        String roomType,
        int roomNumber,
        int numberOfBeds,
        int pricePerNight
) implements Serializable {
}
