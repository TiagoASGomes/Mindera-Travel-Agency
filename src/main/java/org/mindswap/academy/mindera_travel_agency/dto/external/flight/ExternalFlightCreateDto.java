package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

public record ExternalFlightCreateDto (
        String fName,
        String email,
        String phone,
        Long flightId,
        Long priceId
){
}
