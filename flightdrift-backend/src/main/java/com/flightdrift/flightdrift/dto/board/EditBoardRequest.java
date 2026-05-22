package com.flightdrift.flightdrift.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditBoardRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 2048, message = "Description must not exceed 2048 characters")
        String description
) {
}
