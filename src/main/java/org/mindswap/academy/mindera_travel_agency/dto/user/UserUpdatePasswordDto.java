package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.INVALID_PASSWORD;

public record UserUpdatePasswordDto(
        @NotNull(message = INVALID_PASSWORD)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", message = INVALID_PASSWORD)
        @Schema(example = "old123Password", description = "The old password")
        String oldPassword,
        @NotNull(message = INVALID_PASSWORD)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", message = INVALID_PASSWORD)
        @Schema(example = "updated123Password", description = "The new password")
        String newPassword
) {
}
