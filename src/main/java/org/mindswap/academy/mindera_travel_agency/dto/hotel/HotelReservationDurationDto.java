package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_ARRIVAL_DATE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_LEAVE_DATE;

public record HotelReservationDurationDto(
        @FutureOrPresent(message = INVALID_ARRIVAL_DATE)
        @NotNull(message = INVALID_ARRIVAL_DATE)
        LocalDate arrivalDate,
        @Future(message = INVALID_LEAVE_DATE)
        @NotNull(message = INVALID_LEAVE_DATE)
        LocalDate leaveDate
) {

}
