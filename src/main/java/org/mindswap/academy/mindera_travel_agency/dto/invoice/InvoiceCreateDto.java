package org.mindswap.academy.mindera_travel_agency.dto.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_USER_ID;

public record InvoiceCreateDto(
        @Min(value = 1, message = INVALID_USER_ID)
        @NotNull(message = INVALID_USER_ID)
        @Schema(description = "The id of the user", example = "1")
        Long userId
) {
}
