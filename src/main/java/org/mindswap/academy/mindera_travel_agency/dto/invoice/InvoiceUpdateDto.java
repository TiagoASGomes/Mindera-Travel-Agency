package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_PAYMENT_DATE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_PAYMENT_STATUS;

public record InvoiceUpdateDto(
        @Pattern(regexp = "^[A-Z_]+$", message = INVALID_PAYMENT_STATUS)
        @Schema(description = "The payment status", example = "PAID")
        String paymentStatus,
        @FutureOrPresent(message = INVALID_PAYMENT_DATE)
        @Schema(description = "The payment date", example = "2022-12-31T23:59:59")
        LocalDateTime paymentDate
) {
}
