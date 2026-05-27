package com.flightdrift.flightdrift.dto.auth;

import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 06/05/2026
 */
public record SignupResponse(
        UUID id,
        String name,
        String email,
        String username
) {
}
