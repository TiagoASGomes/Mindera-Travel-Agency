package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationInfoDto(
        @NotNull
        LocalDate arrival,
        @NotNull
        LocalDate departure,
        @NotNull
        String hotelN,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @NotNull
        @Pattern(regexp = "(9[1236][0-9])([0-9]{3})([0-9]{3})")
        int phoneNumber,
        @NotNull
        @Pattern(regexp = " ^[125689]\\d{8}$")
        int vat,
        @NotNull
        List<ExternalCreateRoomReservation> roomReservations
) {
}
