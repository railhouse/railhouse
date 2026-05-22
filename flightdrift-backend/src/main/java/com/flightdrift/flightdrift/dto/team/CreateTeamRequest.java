package com.flightdrift.flightdrift.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 255, message = "Name cannot exceed 255 characters")
        String name,

        String website,

        @Size(max = 1024, message = "Description cannot exceed 1024 characters")
        String description,

        @Size(max = 1024, message = "Picture link cannot exceed 1024 characters")
        String pictureLink
) {
}
