package org.invoicebuilder.users.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Password reset request to initiate password recovery.
 */
@Schema(description = "Password reset request to initiate password recovery")
public record ForgotPasswordResetRequest(
    
    @Schema(description = "User's email address for sending password reset link (email_address)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED, name = "email_address")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @JsonProperty("email_address")
    String email
) {}
