package com.flightdrift.flightdrift.controller;

import com.flightdrift.flightdrift.dto.ApiResponse;
import com.flightdrift.flightdrift.dto.auth.SigninRequest;
import com.flightdrift.flightdrift.dto.auth.SignupRequest;
import com.flightdrift.flightdrift.entity.Account;
import com.flightdrift.flightdrift.entity.BlacklistedAuthToken;
import com.flightdrift.flightdrift.entity.OrganizationRole;
import com.flightdrift.flightdrift.repository.AccountRepository;
import com.flightdrift.flightdrift.repository.BlacklistedAuthTokenRepository;
import com.flightdrift.flightdrift.service.AppSettingsService;
import com.flightdrift.flightdrift.service.InvitationService;
import com.flightdrift.flightdrift.service.OrganizationService;
import com.flightdrift.flightdrift.util.JwtUtil;
import com.flightdrift.flightdrift.dto.auth.InvitationTokenResponse;
import com.flightdrift.flightdrift.dto.auth.SignupResponse;
import com.flightdrift.flightdrift.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.flightdrift.flightdrift.config.filter.JwtRequestFilter.AUTHORIZATION_HEADER_PREFIX;
import static com.flightdrift.flightdrift.config.filter.JwtRequestFilter.BEARER_PREFIX;
import static com.flightdrift.flightdrift.entity.OrganizationRole.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.StringUtils.hasText;

/*
 * Author: Jamius Siam
 * Since: 05/05/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final InvitationService invitationService;

    private final AppSettingsService appSettingsService;

    private final PasswordEncoder passwordEncoder;

    private final BlacklistedAuthTokenRepository blacklistedAuthTokenRepository;

    private final CacheManager cacheManager;

    private final OrganizationService organizationService;

    @PostMapping("/signin")
    @Operation(summary = "Sign in")
    public ResponseEntity<ApiResponse<?>> signin(@Valid @RequestBody SigninRequest signinRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.username(), signinRequest.password())
            );
        } catch (BadCredentialsException exception) {
            log.info("Invalid sign in credentials for username: {}", signinRequest.username());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid username or password", null));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(signinRequest.username());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Signed in successfully",
                new TokenResponse(token)
        ));
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up")
    @Transactional
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        boolean hasInvitationCode = hasText(signupRequest.invitationCode());
        boolean firstUser = accountRepository.isFirstUser();

        if (!appSettingsService.isSignupEnabled() && !hasInvitationCode && !firstUser) {
            log.info("Signup is disabled and no invitation code provided");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "Signup is disabled without invitation code", null));
        }

        if (hasInvitationCode) {
            if (!invitationService.isInvitationCodeValid(signupRequest.invitationCode())) {
                log.info("Invalid invitation code: {} Username: {}", signupRequest.invitationCode(), signupRequest.username());

                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid invitation code", null));
            }
        }

        if (accountRepository.existsByUsername(signupRequest.username())) {
            log.info("Username is already taken: {}", signupRequest.username());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Username is already taken", null));
        }

        if (accountRepository.existsByEmail(signupRequest.email())) {
            log.info("Email is already taken: {}", signupRequest.email());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Email is already taken", null));
        }

        Account account = Account.builder()
                .name(signupRequest.name())
                .email(signupRequest.email())
                .username(signupRequest.username())
                .password(passwordEncoder.encode(signupRequest.password()))
                .build();

        Account savedAccount = accountRepository.save(account);

        if (hasInvitationCode) {
            invitationService.getOrganizationIdFromInvitationCode(signupRequest.invitationCode())
                    .ifPresent(organizationId -> organizationService.addAccountToOrganization(account.getUsername(), organizationId, REGULAR_USER));
        }

        return ResponseEntity.status(CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Account created successfully",
                        new SignupResponse(
                                savedAccount.getId(),
                                savedAccount.getName(),
                                savedAccount.getEmail(),
                                savedAccount.getUsername()
                        )
                ));
    }

    @GetMapping("/invitation-token/{organizationId}")
    @Hidden
    public ResponseEntity<ApiResponse<?>> getInvitationToken(@PathVariable UUID organizationId) {
        String invitationToken = invitationService.createInvitationCode(organizationId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Invitation token created successfully",
                new InvitationTokenResponse(invitationToken)
        ));
    }

    @PostMapping("/signout")
    @Operation(summary = "Sign out")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<?>> signout(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER_PREFIX);
        log.info("Received sign out request with auth header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            blacklistedAuthTokenRepository.save(new BlacklistedAuthToken(token));

            Cache cache = cacheManager.getCache("blacklisted_tokens");
            if (cache != null) {
                cache.evict(token);
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Signed out successfully", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid authorization header", null));
    }
}
