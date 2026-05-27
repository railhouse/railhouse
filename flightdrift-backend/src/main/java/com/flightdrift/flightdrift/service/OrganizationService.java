package com.flightdrift.flightdrift.service;

import com.flightdrift.flightdrift.dto.organization.CreateOrganizationRequest;
import com.flightdrift.flightdrift.dto.organization.EditOrganizationRequest;
import com.flightdrift.flightdrift.dto.organization.OrganizationResponse;
import com.flightdrift.flightdrift.entity.Account;
import com.flightdrift.flightdrift.entity.AccountOrganizationMapping;
import com.flightdrift.flightdrift.entity.Organization;
import com.flightdrift.flightdrift.entity.OrganizationRole;
import com.flightdrift.flightdrift.repository.AccountOrganizationMappingRepository;
import com.flightdrift.flightdrift.repository.AccountRepository;
import com.flightdrift.flightdrift.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.flightdrift.flightdrift.entity.OrganizationRole.ADMIN_USER;
import static org.springframework.http.HttpStatus.*;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final AccountOrganizationMappingRepository accountOrganizationMappingRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public OrganizationResponse createOrganization(String username, CreateOrganizationRequest request) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        Organization organization = Organization.builder()
                .name(request.name())
                .url(request.url())
                .iconUrl(request.iconUrl())
                .build();
        organization = organizationRepository.save(organization);

        AccountOrganizationMapping accountOrganizationMapping = AccountOrganizationMapping.builder()
                .account(account)
                .organization(organization)
                .role(ADMIN_USER)
                .build();
        accountOrganizationMappingRepository.save(accountOrganizationMapping);

        return OrganizationResponse.fromEntity(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getOrganizations(String username) {

        return accountOrganizationMappingRepository.findByAccountUsername(username)
                .stream()
                .map(AccountOrganizationMapping::getOrganization)
                .map(OrganizationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationResponse editOrganization(String username, UUID organizationId, EditOrganizationRequest request) {
        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found or access denied"));

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can edit organization details");
        }

        Organization organization = accountOrganizationMapping.getOrganization();
        organization.setName(request.name());
        organization.setUrl(request.url());
        organization.setIconUrl(request.iconUrl());
        organization = organizationRepository.save(organization);

        return OrganizationResponse.fromEntity(organization);
    }

    @Transactional
    public void deleteOrganization(String username, UUID organizationId) {
        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found or access denied"));

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can delete the organization");
        }

        organizationRepository.delete(accountOrganizationMapping.getOrganization());
    }

    @Transactional
    public void addAccountToOrganization(String username, UUID organizationId, OrganizationRole role) {
        Account targetAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Target user not found"));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found"));

        boolean userAlreadyInOrganization = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, organizationId)
                .isPresent();

        if (userAlreadyInOrganization) {
            return;
        }

        AccountOrganizationMapping newAccountOrganizationMapping = AccountOrganizationMapping.builder()
                .account(targetAccount)
                .organization(organization)
                .role(role)
                .build();

        accountOrganizationMappingRepository.save(newAccountOrganizationMapping);
    }

    @Transactional
    public void leaveOrganization(String requesterUsername, UUID organizationId) {
        AccountOrganizationMapping accountOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(requesterUsername, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "You are not a member of this organization"));

        if (accountOrganizationMapping.getRole() == ADMIN_USER) {
            long adminCount = accountOrganizationMappingRepository
                    .countByOrganizationIdAndRole(organizationId, ADMIN_USER);

            if (adminCount <= 1) {
                throw new ResponseStatusException(BAD_REQUEST, "Cannot leave organization: you are the last admin.");
            }
        }

        accountOrganizationMappingRepository.delete(accountOrganizationMapping);
    }

    @Transactional
    public void removeAccountFromOrganization(String requesterUsername, UUID organizationId, String accountId) {
        AccountOrganizationMapping requesterOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(requesterUsername, organizationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Organization not found or access denied"));

        if (requesterOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can remove members");
        }

        AccountOrganizationMapping targetOrganizationMapping = accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(accountId, organizationId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Target account is not a member of this organization")
                );

        if (targetOrganizationMapping.getRole() == ADMIN_USER) {
            long adminCount = accountOrganizationMappingRepository
                    .countByOrganizationIdAndRole(organizationId, ADMIN_USER);

            if (adminCount <= 1) {
                throw new ResponseStatusException(BAD_REQUEST, "Cannot remove the last admin from the organization.");
            }
        }

        accountOrganizationMappingRepository.delete(targetOrganizationMapping);
    }
}
