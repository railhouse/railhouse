package com.flightdrift.flightdrift.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record EditProjectRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Size(max = 1024, message = "Icon URL must not exceed 1024 characters")
        String iconUrl
) {
}
