package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.User;
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
                .hotelName(dtoReservation.hotelInfo().hotelN())
                .hotelAddress(dtoReservation.hotelInfo().location())
                .hotelPhoneNumber(dtoReservation.hotelInfo().phoneNumber())
                .durationOfStay(dtoReservation.leaveDate().getDayOfMonth() - dtoReservation.arrivalDate().getDayOfMonth())
                .invoice(invoice)
                .build();
    }


    public ExternalReservationCreateDto fromEntityToExternalDto(HotelReservation reservation) {
        User user = reservation.getInvoice().getUser();
        return new ExternalReservationCreateDto(
                reservation.getArrivalDate().toString(),
                reservation.getLeaveDate().toString(),
                reservation.getHotelName(),
                user.getFName(),
                "",
                Integer.parseInt(user.getPhoneNumber()),
                123456789,
                roomInfoConverter.fromEntityListToExternalCreateRoomReservationList(reservation.getRooms())
        );

    }
}
