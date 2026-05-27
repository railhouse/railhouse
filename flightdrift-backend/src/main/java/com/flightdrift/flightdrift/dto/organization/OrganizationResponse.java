package com.flightdrift.flightdrift.dto.organization;

import com.flightdrift.flightdrift.entity.Organization;

import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
public record OrganizationResponse(
        UUID id,
        String name,
        String url,
        String iconUrl
) {
    public static OrganizationResponse fromEntity(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getUrl(),
                organization.getIconUrl()
        );
    }
}
