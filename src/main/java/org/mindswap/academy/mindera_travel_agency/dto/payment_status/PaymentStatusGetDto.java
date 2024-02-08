package org.mindswap.academy.mindera_travel_agency.dto.payment_status;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record PaymentStatusGetDto(
        @Schema(description = "The payment status' id", example = "1")
        Long id,
        @Schema(description = "The status name", example = "PAID")
        String statusName
) implements Serializable {

}
