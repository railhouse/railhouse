package com.flightdrift.flightdrift.repository;

import com.flightdrift.flightdrift.entity.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Repository
public interface FlagRepository extends JpaRepository<Flag, UUID> {

    Optional<Flag> findByKey(String key);

}
