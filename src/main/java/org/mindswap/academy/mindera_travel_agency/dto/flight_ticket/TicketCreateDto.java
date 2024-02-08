package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record TicketCreateDto(
        @NotNull(message = INVALID_NAME)
        @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = INVALID_NAME)
        @Schema(description = "The full name of the passenger", example = "John Doe")
        String fName,
        @NotNull(message = INVALID_EMAIL)
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
        @Schema(description = "The email of the passenger", example = "email@example.com")
        String email,
        @NotNull(message = INVALID_PHONE_NUMBER)
        @Pattern(regexp = "^((\\+351|00351|351)?) ?(9[3621])\\d{7}$", message = INVALID_PHONE_NUMBER)
        @Schema(description = "The phone number of the passenger", example = "912345678")
        String phone,
        @NotNull(message = INVALID_FARE_CLASS)
        @Pattern(regexp = "^[A-Z ]+$", message = INVALID_FARE_CLASS)
        @Schema(description = "The fare class of the ticket", example = "ECONOMY")
        String fareClass,
        @NotNull(message = INVALID_PRICE)
        @Min(value = 0, message = INVALID_PRICE)
        @Schema(description = "The price of the ticket", example = "100")
        Integer price,
        @NotNull(message = INVALID_LUGGAGE_WEIGHT)
        @Min(value = 0, message = INVALID_LUGGAGE_WEIGHT)
        @Max(value = 100, message = INVALID_LUGGAGE_WEIGHT)
        @Schema(description = "The maximum luggage weight of the ticket", example = "20")
        Integer maxLuggageWeight,
        @NotNull(message = INVALID_CARRY_ON_LUGGAGE)
        @Schema(description = "If the user has carry-on luggage", example = "true")
        Boolean carryOnLuggage,
        @NotNull(message = INVALID_DURATION)
        @Min(value = 0, message = INVALID_DURATION)
        @Max(value = 24, message = INVALID_DURATION)
        @Schema(description = "The duration of the flight", example = "2.5")
        Float duration,
        @NotNull(message = INVALID_INVOICE_ID)
        @Min(value = 1, message = INVALID_INVOICE_ID)
        @Schema(description = "The id of the invoice", example = "1")
        Long invoiceId,
        @NotNull(message = INVALID_FLIGHT_ID)
        @Min(value = 1, message = INVALID_FLIGHT_ID)
        @Schema(description = "The id of the flight", example = "1")
        Long flightId,
        @NotNull(message = INVALID_PRICE_ID)
        @Min(value = 1, message = INVALID_PRICE_ID)
        @Schema(description = "The id of the price", example = "1")
        Long priceId
) {
}
