package com.flightdrift.flightdrift.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record CreateOrganizationRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        String name,

        String url,

        @Size(max = 1024, message = "Icon URL cannot exceed 1024 characters")
        String iconUrl
) {
}
