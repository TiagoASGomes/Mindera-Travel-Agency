package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationInfoDto(
        @Schema(description = "The arrival date", example = "2021-12-01")
        LocalDate arrival,
        @Schema(description = "The departure date", example = "2021-12-05")
        LocalDate departure,
        @Schema(description = "The hotel name", example = "Hotel Mindera")
        String hotelN,
        @Schema(description = "The user's full name", example = "John Doe")
        String fName,
        @Schema(description = "The user's phone number", example = "+351 123 456 789")
        String phoneNumber,
        @Schema(description = "The user's vat number", example = "123456789")
        String vat,
        @Schema(description = "The rooms the user reserved with updated information")
        List<ExternalRoomInfoDto> roomReservations
) {
}
