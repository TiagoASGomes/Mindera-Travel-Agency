package org.mindswap.academy.mindera_travel_agency.dto.external;

import java.util.Set;

public record ExternalHotelInfoDto(
        Long externalId,
        String name,
        String address,
        int phoneNumber
) {
}
