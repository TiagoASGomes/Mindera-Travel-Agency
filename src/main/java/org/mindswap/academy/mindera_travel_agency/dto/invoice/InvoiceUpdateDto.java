package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_DATE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_PAYMENT_STATUS;

public record InvoiceUpdateDto(
        //TODO paymentStatus table instead of enum
        @Pattern(regexp = "NOT_REQUESTED|PENDING|COMPLETED|CANCELLED", message = INVALID_PAYMENT_STATUS)
        String paymentStatus,
        @FutureOrPresent(message = INVALID_DATE)
        LocalDateTime paymentDate
) {
}
