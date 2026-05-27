package com.flightdrift.flightdrift.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 * Author: Jamius Siam
 * Since: 05/05/2026
 */
@Slf4j
@Service
public class InvitationService {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int INVITATION_TOKEN_LENGTH = 6;
    private final Cache<String, UUID> CACHE;

    public InvitationService() {
        CACHE = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public String createInvitationCode(UUID organizationId) {
        StringBuilder code = new StringBuilder(INVITATION_TOKEN_LENGTH);
        for (int i = 0; i < INVITATION_TOKEN_LENGTH; i++) {
            code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }

        String invitationCode = code.toString();

        log.info("Generated invitation code: {}", invitationCode);

        CACHE.put(invitationCode, organizationId);
        return invitationCode;
    }

    public Optional<UUID> getOrganizationIdFromInvitationCode(String invitationCode) {
        UUID organizationId = CACHE.getIfPresent(invitationCode);

        if (organizationId == null) {
            log.error("Invitation code not found: {}", invitationCode);
            return Optional.empty();
        }

        return Optional.of(organizationId);
    }

    public boolean isInvitationCodeValid(String invitationCode) {
        log.info("Validating invitation code: {}", invitationCode);

        var invitationOrganizationId = CACHE.getIfPresent(invitationCode);
        return invitationOrganizationId != null;
    }

    public void invalidateInvitationCode(String invitationCode) {
        CACHE.invalidate(invitationCode);
    }
}
