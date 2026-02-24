package org.invoicebuilder.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request for user authentication")
public record LoginRequest(

        @Schema(description = "User's email address (email_address)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED, name = "email_address")
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @JsonProperty("email_address")
        String email,

        @Schema(description = "User's password (password)", example = "MySecurePassword123!", requiredMode = Schema.RequiredMode.REQUIRED, format = "password", name = "password")
        @NotBlank(message = "Password is required")
        @JsonProperty("password")
        String password
) {}
