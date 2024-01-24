package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.HotelInfoDto;

public record HotelReservationCreateDto(
        HotelInfoDto hotelInfo,
        ExternalRoomInfoDto roomInfo
) {
}
