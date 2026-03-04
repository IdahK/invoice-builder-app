package org.invoicebuilder.users.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.invoicebuilder.users.domain.Account;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.dto.auth.PasswordChangeRequest;
import org.invoicebuilder.users.dto.auth.PasswordChangeResponse;
import org.invoicebuilder.users.dto.user.CreateUserRequest;
import org.invoicebuilder.users.dto.auth.RegisterRequest;
import org.invoicebuilder.users.dto.auth.RegistrationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountNameService accountNameService;
    private final UserService userService;
    private final AccountService accountService;
    private final UserRoleService userRoleService;

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

    public PasswordChangeResponse changePassword(@NotNull PasswordChangeRequest request) {
        return null;
    }

}
