package org.mindswap.academy.mindera_travel_agency.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Message.*;

public record UserCreateDto(
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
        @Schema(example = "email@example.com")
        String email,
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", message = INVALID_PASSWORD)
        @Schema(example = "zxlmn!!23K")
        String password,
        @Pattern(regexp = "^[A-Za-z]+$", message = INVALID_USER_NAME)
        @Schema(example = "Name")
        String userName,
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$", message = INVALID_DATE_OF_BIRTH)
        @Schema(example = "01/01/1900")
        String dateOfBirth,
        @Pattern(regexp = "^\\+351\\s?9[1236]\\d{7}$", message = INVALID_PHONE_NUMBER)
        @Schema(example = "00351 998321432")
        int phoneNumber
) {
}
