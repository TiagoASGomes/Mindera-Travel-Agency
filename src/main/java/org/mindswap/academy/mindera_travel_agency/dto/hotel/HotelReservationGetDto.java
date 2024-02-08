package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record HotelReservationGetDto(
        @Schema(description = "The hotel reservation's id", example = "1")
        Long id,
        @Schema(description = "The hotel's name", example = "Hotel_name")
        String hotelName,
        @Schema(description = "The hotel's address", example = "Hotel_address")
        String hotelAddress,
        @Schema(description = "The hotel's phone number", example = "912345678")
        String hotelPhoneNumber,
        @Schema(description = "The total price per night", example = "100")
        int pricePerNight,
        @Schema(description = "The duration of the stay in days", example = "3")
        int durationOfStay,
        @Schema(description = "The total price of the reservation", example = "300")
        int totalPrice,
        @Schema(description = "The arrival date", example = "2022-12-31")
        LocalDate arrivalDate,
        @Schema(description = "The leave date", example = "2023-01-01")
        LocalDate leaveDate,
        @Schema(description = "The hotel's rooms")
        List<RoomInfoGetDto> rooms
) implements Serializable {
}
