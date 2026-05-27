package com.flightdrift.flightdrift.controller;

import com.flightdrift.flightdrift.dto.ApiResponse;
import com.flightdrift.flightdrift.dto.project.CreateProjectRequest;
import com.flightdrift.flightdrift.dto.project.EditProjectRequest;
import com.flightdrift.flightdrift.dto.project.ProjectListResponse;
import com.flightdrift.flightdrift.dto.project.ProjectResponse;
import com.flightdrift.flightdrift.dto.project.ProjectWrapperResponse;
import com.flightdrift.flightdrift.service.ProjectService;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Projects")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/organizations/{organizationId}/projects")
    @Operation(summary = "Create a project")
    public ResponseEntity<ApiResponse<?>> createProject(
            Authentication authentication,
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateProjectRequest request) {

        String username = authentication.getName();
        ProjectResponse project = projectService.createProject(username, organizationId, request);

        return ResponseEntity.status(CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Project created successfully",
                                new ProjectWrapperResponse(project)
                        )
                );
    }

    @GetMapping("/organizations/{organizationId}/projects")
    @Operation(summary = "Get all projects for an organization")
    public ResponseEntity<ApiResponse<?>> getProjects(
            Authentication authentication,
            @PathVariable UUID organizationId) {

        String username = authentication.getName();
        List<ProjectResponse> projects = projectService.getProjects(username, organizationId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Projects retrieved successfully", new ProjectListResponse(projects))
        );
    }

    @PutMapping("/projects/{id}")
    @Operation(summary = "Edit a project")
    public ResponseEntity<ApiResponse<?>> editProject(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody EditProjectRequest request) {

        String username = authentication.getName();
        ProjectResponse project = projectService.editProject(username, id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project updated successfully",
                        new ProjectWrapperResponse(project)
                )
        );
    }

    @DeleteMapping("/projects/{id}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<ApiResponse<?>> deleteProject(
            Authentication authentication,
            @PathVariable UUID id) {

        String username = authentication.getName();
        projectService.deleteProject(username, id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Project deleted successfully", null)
        );
    }
}
