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
    private final Cache<String, Boolean> CACHE;

    public InvitationService() {
        CACHE = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public String getInvitation() {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 8; i++) {
            code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }

        String invitationCode = code.toString();

        log.info("Generated invitation code: {}", invitationCode);

        CACHE.put(invitationCode, true);
        return invitationCode;
    }

    public boolean isInvitationValid(String invitationCode) {
        log.info("Validating invitation code: {}", invitationCode);

        boolean isValidCode = CACHE.getIfPresent(invitationCode) != null;
        CACHE.invalidate(invitationCode);

        return isValidCode;
    }

    public void invalidateInvitation(String invitationCode) {
        CACHE.invalidate(invitationCode);
    }
}
