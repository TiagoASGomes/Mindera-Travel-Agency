package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserGetDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Joe")
        String userName,
        @Schema(example = "email@example.com")
        String email,
        @Schema(example = "01/01/1900")
        LocalDate dateOfBirth,
        @Schema(example = "351 9********")
        String phoneNumber
) {
}
