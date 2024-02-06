package org.mindswap.academy.mindera_travel_agency.dto.external;

import java.util.Set;

public record ExternalHotelInfoDto(
        String name,
        String location,
        int phoneNumber,
        Set<ExternalRoomInfoDto> rooms

) {
}
