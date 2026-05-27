package com.flightdrift.flightdrift.controller;

import com.flightdrift.flightdrift.dto.ApiResponse;
import com.flightdrift.flightdrift.dto.organization.CreateOrganizationRequest;
import com.flightdrift.flightdrift.dto.organization.EditOrganizationRequest;
import com.flightdrift.flightdrift.dto.organization.OrganizationListResponse;
import com.flightdrift.flightdrift.dto.organization.OrganizationResponse;
import com.flightdrift.flightdrift.dto.organization.OrganizationWrapperResponse;
import com.flightdrift.flightdrift.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @Operation(summary = "Create an organization")
    public ResponseEntity<ApiResponse<?>> createOrganization(
            Authentication authentication,
            @Valid @RequestBody CreateOrganizationRequest request) {

        String username = authentication.getName();
        OrganizationResponse organization = organizationService.createOrganization(username, request);

        return ResponseEntity
                .status(CREATED)
                .body(new ApiResponse<>(
                                true,
                                "Organization created successfully",
                                new OrganizationWrapperResponse(organization)
                        )
                );
    }

    @GetMapping
    @Operation(summary = "Get all organizations for the user")
    public ResponseEntity<ApiResponse<?>> getOrganizations(Authentication authentication) {
        String username = authentication.getName();
        List<OrganizationResponse> organizations = organizationService.getOrganizations(username);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Organizations retrieved successfully",
                        new OrganizationListResponse(organizations)
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit an organization")
    public ResponseEntity<ApiResponse<?>> editOrganization(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody EditOrganizationRequest request) {

        String username = authentication.getName();
        OrganizationResponse organization = organizationService.editOrganization(username, id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Organization updated successfully",
                        new OrganizationWrapperResponse(organization)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organization")
    public ResponseEntity<ApiResponse<?>> deleteOrganization(
            Authentication authentication,
            @PathVariable UUID id) {

        String username = authentication.getName();
        organizationService.deleteOrganization(username, id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Organization deleted successfully", null)
        );
    }

    @PostMapping("/{id}/leave")
    @Operation(summary = "Leave an organization")
    public ResponseEntity<ApiResponse<?>> leaveOrganization(
            Authentication authentication,
            @PathVariable UUID id) {

        String requesterUsername = authentication.getName();
        organizationService.leaveOrganization(requesterUsername, id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Left organization successfully", null)
        );
    }

    @DeleteMapping("/{id}/members/{usernameToRemove}")
    @Operation(summary = "Remove a member from an organization")
    public ResponseEntity<ApiResponse<?>> removeAccountFromOrganization(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable String usernameToRemove) {

        String requesterUsername = authentication.getName();
        organizationService.removeAccountFromOrganization(requesterUsername, id, usernameToRemove);

        return ResponseEntity.ok(new ApiResponse<>(true, "Member removed successfully", null));
    }
}
