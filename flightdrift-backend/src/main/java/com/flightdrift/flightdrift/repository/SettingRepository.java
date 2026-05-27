package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, UUID> {

    Optional<Setting> findByAccountId(UUID accountId);

}
