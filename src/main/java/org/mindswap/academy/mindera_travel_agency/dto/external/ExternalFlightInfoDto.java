package org.mindswap.academy.mindera_travel_agency.dto.external;

import java.time.LocalDateTime;

public record ExternalFlightInfoDto(
        Long id,
        String origin,
        String destination,
        int duration,
        LocalDateTime dateOfFlight

) {
}
