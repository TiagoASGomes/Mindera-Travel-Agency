package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_ARRIVAL_DATE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_LEAVE_DATE;

public record HotelReservationDurationDto(
        @FutureOrPresent(message = INVALID_ARRIVAL_DATE)
        @NotNull(message = INVALID_ARRIVAL_DATE)
        @Schema(description = "The arrival date", example = "2022-12-31")
        LocalDate arrivalDate,
        @Future(message = INVALID_LEAVE_DATE)
        @NotNull(message = INVALID_LEAVE_DATE)
        @Schema(description = "The leave date", example = "2023-01-01")
        LocalDate leaveDate
) {

}
