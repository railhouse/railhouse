package com.flightdrift.flightdrift.service;

import com.flightdrift.flightdrift.entity.Flag;
import com.flightdrift.flightdrift.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * Author: Jamius Siam
 * Since: 05/05/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final FlagRepository flagRepository;

    @Value("${flightdrift.app.flags.signup-enabled}")
    private boolean signupEnabled;

    public boolean isSignupEnabled() {
        Optional<Flag> signupFlag = flagRepository.findByKey("signup-enabled");

        return signupFlag
                .map(flag -> Boolean.parseBoolean(String.valueOf(flag.getValue())))
                .orElseGet(() -> signupEnabled);
    }
}
