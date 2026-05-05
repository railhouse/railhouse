package dev.jamius.weaver.dto.auth;

public record SignupResponse(
        Long id,
        String name,
        String email,
        String username
) {
}
