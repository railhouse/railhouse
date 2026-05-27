package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOrganizationId(UUID organizationId);
}
