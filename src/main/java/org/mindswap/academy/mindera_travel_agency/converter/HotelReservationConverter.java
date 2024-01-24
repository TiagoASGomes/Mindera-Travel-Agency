package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelReservationConverter {

    @Autowired
    private RoomInfoConverter roomInfoConverter;

    public HotelReservationGetDto fromEntityToGetDto(HotelReservation hotelReservation) {
        return new HotelReservationGetDto(
                hotelReservation.getId(),
                hotelReservation.getHotelName(),
                hotelReservation.getHotelAddress(),
                hotelReservation.getHotelPhoneNumber(),
                hotelReservation.getPricePerNight(),
                hotelReservation.getDurationOfStay(),
                hotelReservation.getTotalPrice(),
                hotelReservation.getCheckInDate(),
                hotelReservation.getCheckOutDate(),
                roomInfoConverter.fromEntityListToGetDtoList(hotelReservation.getRooms()));
    }

    public List<HotelReservationGetDto> fromEntityListToGetDtoList(List<HotelReservation> hotelReservations) {
        if (hotelReservations == null) return new ArrayList<>();
        return hotelReservations.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public HotelReservation fromCreateDtoToEntity(HotelReservationCreateDto hotelReservation) {
        return HotelReservation.builder()
                .checkInDate(hotelReservation.checkInDate())
                .checkOutDate(hotelReservation.checkOutDate())
                .hotelName(hotelReservation.hotelInfo().name())
                .hotelAddress(hotelReservation.hotelInfo().address())
                .hotelPhoneNumber(hotelReservation.hotelInfo().phoneNumber())
                .hotelId(hotelReservation.hotelInfo().id())
                .build();
    }
}
