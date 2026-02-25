package org.invoicebuilder.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Registration response containing user and account information")
@Builder
public record UserRegistrationResponse(
    
    @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("user_id")
    UUID userId,
    
    @Schema(description = "Account ID", example = "550e8400-e29b-41d4-a716-446655440001")
    @JsonProperty("account_id")
    UUID accountId,
    
    @Schema(description = "User email address", example = "user@example.com")
    @JsonProperty("email")
    String email,
    
    @Schema(description = "Account name", example = "Acme Corporation")
    @JsonProperty("account_name")
    String accountName,
    
    @Schema(description = "User display name", example = "John Doe")
    @JsonProperty("display_name")
    String displayName,
    
    @Schema(description = "Registration success message", example = "Registration successful. Please check your email to verify your account.")
    @JsonProperty("message")
    String message
) {}
