package org.mindswap.academy.mindera_travel_agency.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

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
        @Past(message = INVALID_DATE_OF_BIRTH)
        @Pattern(regexp = "^(0[1-9]|[12]'\\d'|3[01])/(0[1-9]|1[0-2])/(\\d{4})$", message = INVALID_DATE_OF_BIRTH)// Falta para o mês de fevereiro e para distinguir meses de 30 ou 31 dias
        @Schema(example = "01/01/1900")
        LocalDate dateOfBirth,
        @Pattern(regexp = "^\\+351\\s?9[1236]\\d{7}$", message = INVALID_PHONE_NUMBER) //falta para numeros fixos
        @Schema(example = "351 923456789")
        int phoneNumber
) {

}
