package org.mindswap.academy.mindera_travel_agency.dto.external;

import java.util.List;

public record ExternalPlaneInfoDto(
        Long id,
        int peopleCapacity,
        int luggageCapacity,
        String companyOwner,
        String modelName,
        ExternalPlaneInfoDto plane,
        List<ExternalPriceInfoDto> prices
) {
}
