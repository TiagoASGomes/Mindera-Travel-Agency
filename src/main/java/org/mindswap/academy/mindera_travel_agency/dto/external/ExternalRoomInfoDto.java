package org.mindswap.academy.mindera_travel_agency.dto.external;

public record ExternalRoomInfoDto(
        Long externalId,
        int pricePerNight,
        String roomType,
        int numberOfBeds
) {
}
