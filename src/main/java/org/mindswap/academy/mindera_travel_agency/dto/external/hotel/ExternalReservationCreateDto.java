package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationCreateDto(
        String arrival,
        String departure,
        String hotelN,
        String fName,
        String phoneNumber,
        String vat,
        List<ExternalCreateRoomReservation> roomReservations
) {
}
