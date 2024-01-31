package org.mindswap.academy.mindera_travel_agency.dto.flight_ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record TicketUpdatePersonalInfo(
        //TODO mexer nos parametros obrigatorios
        @NotNull(message = INVALID_NAME)
        @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = INVALID_NAME)
        String fName,
        @NotNull(message = INVALID_EMAIL)
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
        String email,
        @NotNull(message = INVALID_PHONE_NUMBER)
        @Pattern(regexp = "^((\\+351|00351|351)?) ?(9[3621])\\d{7}$", message = INVALID_PHONE_NUMBER)
        String phone
) {
}
