package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_SEAT_NUMBER;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_TICKET_NUMBER;

public record FlightTicketInfoUpdateDto(
        @NotNull(message = INVALID_TICKET_NUMBER)
        Long ticketNumber,
        @NotNull(message = INVALID_SEAT_NUMBER)
        @Pattern(regexp = "", message = INVALID_SEAT_NUMBER)
        String seatNumber
) {
}
