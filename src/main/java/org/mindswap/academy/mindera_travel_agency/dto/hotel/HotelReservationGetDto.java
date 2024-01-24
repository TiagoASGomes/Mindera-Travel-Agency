package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;

import java.time.LocalDateTime;
import java.util.List;

public record HotelReservationGetDto(
        Long id,
        String hotelName,
        String hotelAddress,
        int hotelPhoneNumber,
        int pricePerNight,
        int durationOfStay,
        int totalPrice,
        LocalDateTime checkInDate,
        LocalDateTime checkOutDate,
        List<RoomInfoGetDto> rooms
) {
}
