package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import java.io.Serializable;

public record TicketGetDto(
        Long id,
        String fName,
        String email,
        String phone,
        Long ticketNumber,
        String seatNumber,
        int price,
        String fareClass,
        int maxLuggageWeight,
        boolean carryOnLuggage,
        float duration
) implements Serializable {
}
