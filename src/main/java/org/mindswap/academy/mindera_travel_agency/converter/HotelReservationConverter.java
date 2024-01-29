package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HotelReservationConverter {

    @Autowired
    private RoomInfoConverter roomInfoConverter;
    @Autowired
    private RoomInfoService roomInfoService;

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

    public HotelReservation fromCreateDtoToEntity(HotelReservationCreateDto dtoReservation, Invoice invoice) {
        HotelReservation hotel = HotelReservation.builder()
                .checkInDate(dtoReservation.checkInDate())
                .checkOutDate(dtoReservation.checkOutDate())
                .hotelName(dtoReservation.hotelInfo().name())
                .hotelAddress(dtoReservation.hotelInfo().address())
                .hotelPhoneNumber(dtoReservation.hotelInfo().phoneNumber())
                .hotelId(dtoReservation.hotelInfo().id())
                .pricePerNight(calculatePricePerNight(dtoReservation.hotelInfo().roomInfo()))
                .durationOfStay(dtoReservation.checkOutDate().getDayOfMonth() - dtoReservation.checkInDate().getDayOfMonth())
                .rooms(saveRooms(dtoReservation.hotelInfo().roomInfo()))
                .invoice(invoice)
                .build();
        hotel.setTotalPrice(hotel.getPricePerNight() * hotel.getDurationOfStay());
        return hotel;
    }

    private int calculatePricePerNight(Set<ExternalRoomInfoDto> externalRoomInfoDtos) {
        return externalRoomInfoDtos.stream()
                .mapToInt(ExternalRoomInfoDto::pricePerNight)
                .sum();

    }

    private Set<RoomInfo> saveRooms(Set<ExternalRoomInfoDto> externalRoomInfoDtos) {
        Set<RoomInfo> rooms = roomInfoConverter.fromExternalDtoListToEntityList(externalRoomInfoDtos);
        rooms.forEach(roomInfoService::create);
        return rooms;
    }
}
