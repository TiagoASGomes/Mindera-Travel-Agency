package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalCreateRoomReservation(
        @Schema(description = "The room type", example = "SINGLEROOM")
        String roomType
) {

}
