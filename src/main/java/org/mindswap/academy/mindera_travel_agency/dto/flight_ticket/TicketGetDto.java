package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

public record TicketGetDto(
        Long id,
        String fName,
        String email,
        String phone,
        Long ticketNumber,
        String seatNumber,
        int price,
        int maxLuggageWeight,
        boolean carryOnLuggage
) {
}
