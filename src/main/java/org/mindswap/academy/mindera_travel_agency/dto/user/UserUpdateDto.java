package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record UserUpdateDto(
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
        @Schema(example = "email@example.com", description = "The email of the user")
        String email,
        @Pattern(regexp = "^[\\w]+$", message = INVALID_USER_NAME)
        @Schema(example = "User_test", description = "The name of the user")
        String userName,
        @Pattern(regexp = "^((\\+351|00351|351)?) ?(9[3621])\\d{7}$", message = INVALID_PHONE_NUMBER)
        @Schema(example = "351 923456789", description = "The phone number of the user")
        String phoneNumber,
        @Pattern(regexp = "^[125689]\\d{8}$", message = INVALID_VAT)
        @Schema(example = "123456789", description = "The VAT number of the user")
        String vat
) {

}
