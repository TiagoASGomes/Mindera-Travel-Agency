package org.mindswap.academy.mindera_travel_agency.dto.external;

import java.util.Set;

public record HotelInfoDto(
        String name,
        Set<ExternalRoomInfoDto> roomInfo
) {
}
