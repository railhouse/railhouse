package com.flightdrift.flightdrift.dto.project;

import com.flightdrift.flightdrift.entity.Project;

import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record ProjectResponse(
        UUID id,
        String name,
        String iconUrl,
        UUID organizationId
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getIconUrl(),
                project.getOrganization().getId()
        );
    }
}
