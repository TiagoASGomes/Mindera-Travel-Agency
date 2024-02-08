package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

public record UserCreateDto(
        @NotNull(message = INVALID_EMAIL)
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
        @Schema(description = "The email of the user", example = "email@example.com")
        String email,
        @NotNull(message = INVALID_PASSWORD)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", message = INVALID_PASSWORD)
        @Schema(example = "zxlmn!!23K", description = "Password must contain at least 8 characters, one letter and one number")
        String password,
        @NotNull(message = INVALID_USER_NAME)
        @Pattern(regexp = "^[\\w]+$", message = INVALID_USER_NAME)
        @Schema(example = "User_test", description = "The name of the user")
        String userName,
        @NotNull(message = INVALID_DATE_OF_BIRTH)
        @Past(message = INVALID_DATE_OF_BIRTH)
        @Schema(example = "2000-01-01", description = "The date of birth of the user")
        LocalDate dateOfBirth,
        @NotNull(message = INVALID_PHONE_NUMBER)
        @Pattern(regexp = "^((\\+351|00351|351)?) ?(9[3621])\\d{7}$", message = INVALID_PHONE_NUMBER)
        @Schema(example = "351 923456789", description = "The phone number of the user")
        String phoneNumber,
        @NotNull(message = INVALID_VAT)
        @Pattern(regexp = "^[125689]\\d{8}$", message = INVALID_VAT)
        @Schema(example = "123456789", description = "The VAT number of the user")
        String vat
) {

}
