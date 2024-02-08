package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;

public record UserGetDto(
        @Schema(example = "1", description = "The user's id")
        Long id,
        @Schema(example = "Joe", description = "The user's name")
        String userName,
        @Schema(example = "email@example.com", description = "The user's email")
        String email,
        @Schema(example = "01/01/1900", description = "The user's date of birth")
        LocalDate dateOfBirth,
        @Schema(example = "351 912345678", description = "The user's phone number")
        String phoneNumber
) implements Serializable {
}
