package com.flightdrift.flightdrift.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password,

        String invitationCode
) {
}
