package dev.jamius.weaver.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class InvitationService {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final Cache<String, Long> CACHE;

    public InvitationService() {
        CACHE = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public String createInvitationCode(long teamId) {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 8; i++) {
            code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }

        String invitationCode = code.toString();

        log.info("Generated invitation code: {}", invitationCode);

        CACHE.put(invitationCode, teamId);
        return invitationCode;
    }

    public long getTeamIdFromInvitationCode(String invitationCode) {
        Long teamId = CACHE.getIfPresent(invitationCode);

        if (teamId == null) {
            log.error("Invitation code not found: {}", invitationCode);
            return -1;
        }

        return teamId;
    }

    public boolean isInvitationCodeValid(String invitationCode) {
        log.info("Validating invitation code: {}", invitationCode);

        var invitationTeamId = CACHE.getIfPresent(invitationCode);
        return invitationTeamId != null;
    }

    public void invalidateInvitationCode(String invitationCode) {
        CACHE.invalidate(invitationCode);
    }
}
