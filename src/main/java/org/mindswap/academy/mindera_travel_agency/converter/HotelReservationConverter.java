package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelReservationConverter {

    @Autowired
    private RoomInfoConverter roomInfoConverter;

    public HotelReservationGetDto fromEntityToGetDto(HotelReservation hotelReservation) {
        if (hotelReservation == null) return null;
        return new HotelReservationGetDto(
                hotelReservation.getId(),
                hotelReservation.getHotelName(),
                hotelReservation.getHotelAddress(),
                hotelReservation.getHotelPhoneNumber(),
                hotelReservation.getPricePerNight(),
                hotelReservation.getDurationOfStay(),
                hotelReservation.getTotalPrice(),
                hotelReservation.getArrivalDate(),
                hotelReservation.getLeaveDate(),
                roomInfoConverter.fromEntityListToGetDtoList(hotelReservation.getRooms()));
    }

    public List<HotelReservationGetDto> fromEntityListToGetDtoList(List<HotelReservation> hotelReservations) {
        if (hotelReservations == null) return new ArrayList<>();
        return hotelReservations.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public HotelReservation fromCreateDtoToEntity(HotelReservationCreateDto dtoReservation, Invoice invoice) {
        return HotelReservation.builder()
                .arrivalDate(dtoReservation.arrivalDate())
                .leaveDate(dtoReservation.leaveDate())
                .hotelName(dtoReservation.hotelInfo().name())
                .hotelAddress(dtoReservation.hotelInfo().address())
                .hotelPhoneNumber(dtoReservation.hotelInfo().phoneNumber())
                .externalId(dtoReservation.hotelInfo().externalId())
                .durationOfStay(dtoReservation.leaveDate().getDayOfMonth() - dtoReservation.arrivalDate().getDayOfMonth())
                .invoice(invoice)
                .externalId(dtoReservation.hotelInfo().externalId())
                .build();
    }


}
