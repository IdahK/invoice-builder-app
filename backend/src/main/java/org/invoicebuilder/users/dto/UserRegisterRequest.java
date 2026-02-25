package org.invoicebuilder.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * User registration request for creating new accounts with account-first approach.
 * Supports both business and personal user registration.
 */
@Schema(description = "User registration request for creating new accounts")
public record UserRegisterRequest(
    
    @Schema(description = "Account name for organization (optional for personal users)", example = "Acme Corporation", name = "account_name")
//    @ValidAccountName
    @JsonProperty("account_name")
    String accountName,
    
    @Schema(description = "User's email address (email_address)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED, name = "email_address")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @JsonProperty("email_address")
    String email,
    
    @Schema(description = "User's password (password)", example = "MySecurePassword123!", requiredMode = Schema.RequiredMode.REQUIRED, format = "password", minLength = 8, maxLength = 128, name = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @JsonProperty("password")
    String password,
    
    @Schema(description = "User's display name (display_name)", example = "John Doe", maxLength = 100, name = "display_name", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    @JsonProperty("display_name")
    String displayName
) {}
