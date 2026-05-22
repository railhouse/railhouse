package com.flightdrift.flightdrift.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SigninRequest(
        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
