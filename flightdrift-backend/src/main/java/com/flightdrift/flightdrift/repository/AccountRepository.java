package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    default boolean isFirstUser() {
        return count() == 0;
    }
}
