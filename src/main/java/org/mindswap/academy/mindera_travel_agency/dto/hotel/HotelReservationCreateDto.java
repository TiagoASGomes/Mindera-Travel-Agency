package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalHotelInfoDto;

import java.time.LocalDateTime;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record HotelReservationCreateDto(
        @FutureOrPresent(message = INVALID_DATE)
        @NotNull(message = INVALID_DATE)
        LocalDateTime arrivalDate,
        @Future(message = INVALID_DATE)
        @NotNull(message = INVALID_DATE)
        LocalDateTime leaveDate,
        @NotNull(message = INVALID_HOTEL_INFO)
        ExternalHotelInfoDto hotelInfo,
        @Min(value = 1, message = INVALID_ID)
        @NotNull(message = INVALID_ID)
        Long invoiceId
) {
}
