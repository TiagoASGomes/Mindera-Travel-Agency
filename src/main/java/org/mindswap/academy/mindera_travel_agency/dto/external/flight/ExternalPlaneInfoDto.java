package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPlaneInfoDto(
        Long id,
        int peopleCapacity,
        int luggageCapacity,
        String companyOwner,
        String modelName
) {
}
