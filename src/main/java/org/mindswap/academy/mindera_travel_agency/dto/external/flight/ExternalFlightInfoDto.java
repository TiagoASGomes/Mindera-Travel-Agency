package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalFlightInfoDto(
        Long id,
        String origin,
        String destination,
        int duration,
        LocalDateTime dateOfFlight,
        int availableSeats,
        ExternalPlaneInfoDto plane,
        List<ExternalPriceInfoDto> price

) implements Serializable {
}
