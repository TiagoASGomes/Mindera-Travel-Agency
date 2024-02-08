package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record TicketGetDto(
        @Schema(description = "The ticket's unique identifier")
        Long id,
        @Schema(description = "The full name of the passenger", example = "John Doe")
        String fName,
        @Schema(description = "The email of the passenger", example = "email@example.com")
        String email,
        @Schema(description = "The phone number of the passenger", example = "912345678")
        String phone,
        @Schema(description = "The ticket's unique number", example = "1")
        Long ticketNumber,
        @Schema(description = "The seat number of the ticket", example = "1A")
        String seatNumber,
        @Schema(description = "The price of the ticket", example = "100")
        int price,
        @Schema(description = "The fare class of the ticket", example = "ECONOMY")
        String fareClass,
        @Schema(description = "The maximum luggage weight the user can bring", example = "20")
        int maxLuggageWeight,
        @Schema(description = "If the user has carry-on luggage", example = "true")
        boolean carryOnLuggage,
        @Schema(description = "The duration of the flight", example = "2.5")
        float duration
) implements Serializable {
}
