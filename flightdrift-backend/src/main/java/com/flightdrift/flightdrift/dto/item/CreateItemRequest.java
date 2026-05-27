package com.flightdrift.flightdrift.dto.item;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record CreateItemRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        String name,

        @Size(max = 2048, message = "Description must not exceed 2048 characters")
        String description,

        LocalDate startDate,

        LocalDate endDate
) {
    @AssertTrue(message = "End date cannot be before start date")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
