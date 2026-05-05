package dev.jamius.weaver.controller;

import dev.jamius.weaver.dto.ApiResponse;
import dev.jamius.weaver.dto.auth.Signin;
import dev.jamius.weaver.dto.auth.Signup;
import dev.jamius.weaver.entity.Account;
import dev.jamius.weaver.entity.BlacklistedAuthToken;
import dev.jamius.weaver.entity.TeamRole;
import dev.jamius.weaver.repository.AccountRepository;
import dev.jamius.weaver.repository.BlacklistedAuthTokenRepository;
import dev.jamius.weaver.service.AppSettingsService;
import dev.jamius.weaver.service.InvitationService;
import dev.jamius.weaver.service.TeamService;
import dev.jamius.weaver.util.JwtUtil;
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

import java.util.Map;

import static dev.jamius.weaver.config.filter.JwtRequestFilter.AUTHORIZATION_HEADER_PREFIX;
import static dev.jamius.weaver.config.filter.JwtRequestFilter.BEARER_PREFIX;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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
    private final TeamService teamService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signin(@Valid @RequestBody Signin signin) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signin.username(), signin.password())
            );
        } catch (BadCredentialsException exception) {
            log.info("Invalid sign in credentials for username: {}", signin.username());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid username or password", null));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(signin.username());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Signed in successfully",
                Map.of("token", token)
        ));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody Signup signup) {
        boolean hasInvitationCode = hasText(signup.invitationCode());
        boolean firstUser = accountRepository.isFirstUser();

        if (!appSettingsService.isSignupEnabled() && !hasInvitationCode && !firstUser) {
            log.info("Signup is disabled and no invitation code provided");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "Signup is disabled without invitation code", null));
        }

        if (hasInvitationCode) {
            if (!invitationService.isInvitationCodeValid(signup.invitationCode())) {
                log.info("Invalid invitation code: {} Username: {}", signup.invitationCode(), signup.username());

                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid invitation code", null));
            }
        }

        if (accountRepository.existsByUsername(signup.username())) {
            log.info("Username is already taken: {}", signup.username());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Username is already taken", null));
        }

        if (accountRepository.existsByEmail(signup.email())) {
            log.info("Email is already taken: {}", signup.email());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Email is already taken", null));
        }

        Account account = Account.builder()
                .name(signup.name())
                .email(signup.email())
                .username(signup.username())
                .password(passwordEncoder.encode(signup.password()))
                .build();

        Account savedAccount = accountRepository.save(account);

        long invitedTeamId =  invitationService.getTeamIdFromInvitationCode(signup.invitationCode());

        if (invitedTeamId != -1) {
            teamService.addAccountToTeam(account.getUsername(), invitedTeamId, TeamRole.MEMBER);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Account created successfully",
                        Map.of(
                                "id", savedAccount.getId(),
                                "name", savedAccount.getName(),
                                "email", savedAccount.getEmail(),
                                "username", savedAccount.getUsername()
                        )
                ));
    }

    @GetMapping("/invitation-token/{teamId}")
    public ResponseEntity<ApiResponse<?>> getInvitationToken(@PathVariable long teamId) {
        String invitationToken = invitationService.createInvitationCode(teamId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Invitation token created successfully",
                Map.of("invitationToken", invitationToken)
        ));
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<?>> signout(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER_PREFIX);
        log.info("Received signout request with auth header: {}", authHeader);

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
