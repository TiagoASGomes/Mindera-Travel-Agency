package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_DATE;

public record HotelReservationDurationDto(
        @FutureOrPresent(message = INVALID_DATE)
        @NotNull(message = INVALID_DATE)
        LocalDateTime checkInDate,
        @Future(message = INVALID_DATE)
        @NotNull(message = INVALID_DATE)
        LocalDateTime checkOutDate
) {
}
