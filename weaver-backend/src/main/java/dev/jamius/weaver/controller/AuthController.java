package dev.jamius.weaver.controller;

import dev.jamius.weaver.dto.ApiResponse;
import dev.jamius.weaver.dto.auth.Signup;
import dev.jamius.weaver.entity.Account;
import dev.jamius.weaver.repository.AccountRepository;
import dev.jamius.weaver.service.AppSettingsService;
import dev.jamius.weaver.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountRepository accountRepository;

    private final InvitationService invitationService;

    private final AppSettingsService appSettingsService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/invitation-token")
    public ResponseEntity<ApiResponse<?>> getInvitationToken() {
        String invitationToken = invitationService.getInvitationCode();

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Invitation token created successfully",
                Map.of("invitationToken", invitationToken)
        ));
    }

    @PostMapping("/signup")
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

        Account account = new Account();
        account.setName(signup.name());
        account.setEmail(signup.email());
        account.setUsername(signup.username());
        account.setPassword(passwordEncoder.encode(signup.password()));

        Account savedAccount = accountRepository.save(account);

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
}
