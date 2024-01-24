package org.mindswap.academy.mindera_travel_agency.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
public record UserCreateDto(
        @Schema(example = "email@example.com")
        String email,
        @Schema(example = "true")
        boolean password,
        @Schema(example = "true")
        boolean userName,
        @Schema(example = "true")
        boolean dateOfBirth,
        @Schema(example = "true")
        boolean phoneNumber
) {
}
