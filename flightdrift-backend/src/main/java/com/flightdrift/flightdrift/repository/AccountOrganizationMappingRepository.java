package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.AccountOrganizationMapping;
import com.flightdrift.flightdrift.entity.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface AccountOrganizationMappingRepository extends JpaRepository<AccountOrganizationMapping, UUID> {

    List<AccountOrganizationMapping> findByAccountUsername(String username);

    Optional<AccountOrganizationMapping> findByAccountUsernameAndOrganizationId(String username, UUID organizationId);

    long countByOrganizationIdAndRole(UUID organizationId, OrganizationRole role);
}
