package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record HotelReservationGetDto(
        Long id,
        String hotelName,
        String hotelAddress,
        String hotelPhoneNumber,
        int pricePerNight,
        int durationOfStay,
        int totalPrice,
        LocalDate arrivalDate,
        LocalDate leaveDate,
        List<RoomInfoGetDto> rooms
) implements Serializable {
}
