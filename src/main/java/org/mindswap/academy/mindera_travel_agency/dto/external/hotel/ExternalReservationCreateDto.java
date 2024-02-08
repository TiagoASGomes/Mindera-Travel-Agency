package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationCreateDto(
        @Schema(description = "The arrival date", example = "2022-12-12")
        String arrival,
        @Schema(description = "The departure date", example = "2022-12-15")
        String departure,
        @Schema(description = "The hotel name", example = "Hotel Mindera")
        String hotelN,
        @Schema(description = "The user's full name", example = "John Doe")
        String fullName,
        @Schema(description = "The user's phone number", example = "+351 912 345 678")
        String phoneNumber,
        @Schema(description = "The user's vat number", example = "123456789")
        String vat,
        @Schema(description = "The rooms the user wants to reserve")
        List<ExternalCreateRoomReservation> roomReservations
) {
}
