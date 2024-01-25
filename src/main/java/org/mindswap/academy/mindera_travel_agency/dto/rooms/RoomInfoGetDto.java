package org.mindswap.academy.mindera_travel_agency.dto.rooms;

public record RoomInfoGetDto(
        Long id,
        String roomType,
        String roomNumber,
        int numberOfBeds,
        int pricePerNight
) {
}
