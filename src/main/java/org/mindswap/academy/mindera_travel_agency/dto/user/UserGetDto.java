package org.mindswap.academy.mindera_travel_agency.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserGetDto(
        @Schema(example = "email@example.com")
        String email,
        @Schema(example = "Joe")
        String userName
) {

}
