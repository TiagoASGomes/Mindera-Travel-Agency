package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalRoomInfoDto(
        int roomNumber,
        int numberOfBeds,
        String roomType,
        int roomPrice
) {
}
