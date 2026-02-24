package org.invoicebuilder.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Refresh token request for obtaining new access tokens.
 */
@Schema(description = "Refresh token request for obtaining new access tokens")
public record RefreshTokenRequest(
    
    @Schema(description = "Valid refresh token (refresh_token)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED, name = "refresh_token")
    @NotBlank(message = "Refresh token is required")
    @JsonProperty("refresh_token")
    String refreshToken
) {}
