package org.mindswap.academy.mindera_travel_agency.dto.external;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExternalReservationCreateDto(
        @NotNull
        String arrival,
        @NotNull
        String departure,
        @NotNull
        String hotelN,
        @NotNull
        List<String> roomType,
        @NotNull
        String fName,
        @NotNull
        String phoneNumber
) {
}
