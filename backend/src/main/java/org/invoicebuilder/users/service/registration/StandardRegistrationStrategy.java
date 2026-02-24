package org.invoicebuilder.users.service.registration;

import jakarta.validation.constraints.NotNull;
import org.invoicebuilder.users.domain.Account;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.dto.UserRegisterRequest;
import org.invoicebuilder.users.dto.UserRegistrationResponse;
import org.invoicebuilder.users.dto.UserDtos.CreateUserRequest;
import org.invoicebuilder.users.service.core.AccountNameService;
import org.invoicebuilder.users.service.core.AccountService;
import org.invoicebuilder.users.service.core.RoleService;
import org.invoicebuilder.users.service.core.UserService;
import org.invoicebuilder.users.service.registration.interfaces.RegistrationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Standard registration strategy for email/password user registration
 * Implements the default registration flow with account creation and role assignment
 */
@Service
public class StandardRegistrationStrategy implements RegistrationStrategy {

    @Autowired
    private RegistrationValidationService registrationValidationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountNameService accountNameService;

    @Override
    @Transactional
    public UserRegistrationResponse register(@NotNull UserRegisterRequest request) {
        try {
            return performRegistration(request);
        } catch (DataIntegrityViolationException e) {
            handleDataIntegrityViolation(e);
            throw new IllegalArgumentException("Registration failed due to data conflict. Please try again.");
        }
    }

    private UserRegistrationResponse performRegistration(@NotNull UserRegisterRequest request) {
        // 1. Validate input parameters
        registrationValidationService.validateRegistrationRequest(request);
        registrationValidationService.validateEmailFormat(request.email());
        registrationValidationService.validatePasswordStrength(request.password());

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
        CreateUserRequest createUserRequest = new CreateUserRequest(
                request.email(), request.password(), request.displayName(), account
        );
        User user = userService.createUser(createUserRequest);

        // 6. Assign OWNER role to the user
        roleService.assignRoleToUser(user, "OWNER");

        // 7. TODO: Send verification email (implement EmailService later)
        // emailService.sendVerificationEmail(user);

        return buildRegistrationResponse(user, account);
    }

    private UserRegistrationResponse buildRegistrationResponse(User user, Account account) {
        return UserRegistrationResponse
                .builder()
                .userId(user.getUserId())
                .accountId(account.getAccountId())
                .email(user.getUserEmail())
                .accountName(account.getAccountName())
                .displayName(user.getUserDisplayName())
                .message("Registration successful. Please check your email to verify your account.")
                .build();
    }

    private void handleDataIntegrityViolation(DataIntegrityViolationException e) {
        if (e.getMessage() != null && e.getMessage().contains("account_name")) {
            throw new IllegalArgumentException("Account name already exists. Please try a different name.");
        } else if (e.getMessage() != null && e.getMessage().contains("user_email")) {
            throw new IllegalArgumentException("Email address is already registered.");
        }
    }

    @Override
    public String getStrategyType() {
        return "STANDARD";
    }
}
