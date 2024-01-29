package org.mindswap.academy.mindera_travel_agency.dto.payment_status;

import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_PAYMENT_STATUS;

public record PaymentStatusCreateDto(
        @Pattern(regexp = "^[A-Z_]+$", message = INVALID_PAYMENT_STATUS)
        String statusName
) {
}
