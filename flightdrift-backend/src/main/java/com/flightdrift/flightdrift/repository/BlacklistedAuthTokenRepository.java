package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.BlacklistedAuthToken;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface BlacklistedAuthTokenRepository extends JpaRepository<BlacklistedAuthToken, UUID> {
    
    @Cacheable("blacklisted_tokens")
    Optional<BlacklistedAuthToken> findByToken(String token);
}
