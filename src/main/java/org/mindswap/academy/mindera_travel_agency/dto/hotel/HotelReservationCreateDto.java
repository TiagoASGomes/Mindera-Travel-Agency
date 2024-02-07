package org.mindswap.academy.mindera_travel_agency.dto.hotel;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalHotelInfoDto;

import java.time.LocalDate;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record HotelReservationCreateDto(
        @FutureOrPresent(message = INVALID_ARRIVAL_DATE)
        @NotNull(message = INVALID_ARRIVAL_DATE)
        LocalDate arrivalDate,
        @Future(message = INVALID_LEAVE_DATE)
        @NotNull(message = INVALID_LEAVE_DATE)
        LocalDate leaveDate,
        @NotNull(message = INVALID_HOTEL_INFO)
        ExternalHotelInfoDto hotelInfo,
        @Min(value = 1, message = INVALID_INVOICE_ID)
        @NotNull(message = INVALID_INVOICE_ID)
        Long invoiceId
) {
}
