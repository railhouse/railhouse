package dev.jamius.weaver.repository;

import dev.jamius.weaver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
