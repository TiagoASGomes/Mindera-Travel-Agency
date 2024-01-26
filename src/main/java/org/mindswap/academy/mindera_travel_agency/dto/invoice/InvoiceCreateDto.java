package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_ID;

public record InvoiceCreateDto(
        @Min(value = 1, message = INVALID_ID)
        @NotNull(message = INVALID_ID)
        Long userId
) {
}
