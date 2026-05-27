package com.flightdrift.flightdrift.dto.item;

import com.flightdrift.flightdrift.entity.Item;

import java.time.LocalDate;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record ItemResponse(
        UUID id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        UUID projectId
) {
    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStartDate(),
                item.getEndDate(),
                item.getProject().getId()
        );
    }
}
