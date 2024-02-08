package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalHotelInfoDto(
        String hotelN,
        String location,
        String phoneNumber,
        Set<ExternalRoomInfoDto> rooms
) implements Serializable {
}
