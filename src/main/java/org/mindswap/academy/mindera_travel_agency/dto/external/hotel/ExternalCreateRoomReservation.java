package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalCreateRoomReservation(
        String roomType
) {

}
