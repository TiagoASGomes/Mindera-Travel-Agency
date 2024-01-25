package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

public record FlightTicketCreateDto(
        Long ticketNumber,
        String fName,
        String email,
        String phone,
        String seatNumber,
        int price,
        String fareClass,
        int maxLuggageWeight,
        boolean carryOnLuggage,
        Long invoiceId
) {
}
