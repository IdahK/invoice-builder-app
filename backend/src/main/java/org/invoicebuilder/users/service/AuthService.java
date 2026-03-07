package org.invoicebuilder.users.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.invoicebuilder.config.TokenProperties;
import org.invoicebuilder.users.domain.Account;
import org.invoicebuilder.users.domain.SecurityUser;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.dto.auth.*;
import org.invoicebuilder.users.dto.user.CreateUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountNameService accountNameService;
    private final UserService userService;
    private final AccountService accountService;
    private final UserRoleService userRoleService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;

    @Transactional
    public RegistrationResponse registerEmailUser(@NotNull RegisterRequest request) {

        // 2. Generate account name for personal users or validate business account name
        String accountName = accountNameService.generateOrValidateAccountName(
                request.accountName(), request.email(), request.displayName()
        );

        // 3. Check if user email already exists (business logic validation)
        if (userService.userExistsByEmail(request.email())) {
            throw new IllegalArgumentException("Email '" + request.email() + "' is already registered");
        }

        // 4. Create account first
        Account account = accountService.createAccount(accountName);

        // 5. Create user with account reference
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.email())
                .password(request.password())
                .displayName(request.displayName())
                .account(account)
                .build();
        User user = userService.createUser(createUserRequest);

        // 6. Assign OWNER role to the user
        userRoleService.assignRoleToUser(user, "OWNER");

        // 7. TODO: Send verification email (implement EmailService later)
        // emailService.sendVerificationEmail(user);

        return RegistrationResponse
                .builder()
                .userId(user.getUserId())
                .accountId(account.getAccountId())
                .email(user.getUserEmail())
                .accountName(account.getAccountName())
                .displayName(user.getUserDisplayName())
                .message("Registration successful. Please check your email to verify your account.")
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user =  Objects.requireNonNull(securityUser).getUser();

        String token = tokenService.generateToken(authentication);
        String refreshToken = refreshTokenService.createToken(user);
        return AuthResponse.create(token, refreshToken, tokenProperties.getAccessTokenTtl().getSeconds());
    }

    public PasswordChangeResponse changePassword(@NotNull PasswordChangeRequest request) {
        return null;
    }

}
