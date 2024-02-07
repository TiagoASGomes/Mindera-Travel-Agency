package org.mindswap.academy.mindera_travel_agency.dto.payment_status;

import java.io.Serializable;

public record PaymentStatusGetDto(
        Long id,
        String statusName
) implements Serializable {

}
