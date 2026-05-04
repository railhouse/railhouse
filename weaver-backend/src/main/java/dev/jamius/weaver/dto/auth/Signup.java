package dev.jamius.weaver.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record Signup(
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
