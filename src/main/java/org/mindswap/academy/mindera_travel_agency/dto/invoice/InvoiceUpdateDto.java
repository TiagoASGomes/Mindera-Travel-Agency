package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record InvoiceUpdateDto(
        @Pattern(regexp = "^[A-Z_]+$", message = INVALID_PAYMENT_STATUS)
        String paymentStatus,
        @FutureOrPresent(message = INVALID_DATE)
        LocalDateTime paymentDate
) {
}
