package org.mindswap.academy.mindera_travel_agency.dto.external.flight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalBookingInfoDto (
    Long id,
    String fName,
    String email,
    String phone,
    String seatNumber,
    ExternalFlightInfoDto flight,
    ExternalPriceInfoDto price
){
}
