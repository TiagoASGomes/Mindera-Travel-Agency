package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalHotelInfoDto(
        @Schema(description = "The hotel name", example = "Hotel_Mindera")
        String hotelN,
        @Schema(description = "The hotel location", example = "Porto")
        String location,
        @Schema(description = "The hotel phone number", example = "+351 123 456 789")
        String phoneNumber,
        @Schema(description = "The hotel rooms dtos")
        Set<ExternalRoomInfoDto> rooms
) implements Serializable {
}
