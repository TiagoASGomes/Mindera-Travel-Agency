package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelReservationConverter {

    public HotelReservationGetDto fromEntityToGetDto(HotelReservation hotelReservation) {
        return null;
    }

    public List<HotelReservationGetDto> fromEntityListToGetDtoList(List<HotelReservation> hotelReservations) {
        return null;
    }

    public HotelReservation fromCreateDtoToEntity(HotelReservationCreateDto hotelReservation) {
        return null;
    }
}
