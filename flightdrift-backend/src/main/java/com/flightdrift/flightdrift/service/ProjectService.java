package com.flightdrift.flightdrift.service;

import com.flightdrift.flightdrift.dto.project.CreateProjectRequest;
import com.flightdrift.flightdrift.dto.project.EditProjectRequest;
import com.flightdrift.flightdrift.dto.project.ProjectResponse;
import com.flightdrift.flightdrift.entity.AccountOrganizationMapping;
import com.flightdrift.flightdrift.entity.Organization;
import com.flightdrift.flightdrift.entity.Project;
import com.flightdrift.flightdrift.repository.AccountOrganizationMappingRepository;
import com.flightdrift.flightdrift.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.flightdrift.flightdrift.entity.OrganizationRole.ADMIN_USER;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final AccountOrganizationMappingRepository accountOrganizationMappingRepository;

    @Transactional
    public ProjectResponse createProject(String username, UUID organizationId, CreateProjectRequest request) {
        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found or access denied"));

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can create projects");
        }

        Organization organization = accountOrganizationMapping.getOrganization();

        Project project = Project.builder()
                .name(request.name())
                .iconUrl(request.iconUrl())
                .organization(organization)
                .build();

        project = projectRepository.save(project);

        return ProjectResponse.fromEntity(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(String username, UUID organizationId) {
        accountOrganizationMappingRepository.findByAccountUsernameAndOrganizationId(username, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found or access denied"));

        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        return projects.stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse editProject(String username, UUID projectId, EditProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));

        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, project.getOrganization().getId())
                .orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Access denied"));

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can edit projects");
        }

        project.setName(request.name());
        project.setIconUrl(request.iconUrl());

        project = projectRepository.save(project);

        return ProjectResponse.fromEntity(project);
    }

    @Transactional
    public void deleteProject(String username, UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));

        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, project.getOrganization().getId())
                .orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Access denied"));

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can delete projects");
        }

        projectRepository.delete(project);
    }
}
