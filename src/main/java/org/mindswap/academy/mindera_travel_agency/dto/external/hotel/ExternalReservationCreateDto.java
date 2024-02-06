package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationCreateDto(
        @NotNull
        String arrival,
        @NotNull
        String departure,
        @NotNull
        String hotelN,
        @NotNull
        String firstName,
        String lastName,
        @NotNull
        int phoneNumber,
        int vat,
        @NotNull
        List<ExternalCreateRoomReservation> roomReservations
) {
}
