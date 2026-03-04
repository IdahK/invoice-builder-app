package org.invoicebuilder.users.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.invoicebuilder.users.dto.user.UserSummaryResponse;

/**
 * Authentication response containing tokens and user information.
 */
@Schema(description = "Authentication response containing tokens and user information")
@Builder
public record AuthResponse(
    
    @Schema(description = "JWT access token for API authentication (access_token)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", name = "access_token")
    @JsonProperty("access_token")
    String accessToken,
    
    @Schema(description = "Refresh token for obtaining new access tokens (refresh_token)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", name = "refresh_token")
    @JsonProperty("refresh_token")
    String refreshToken,
    
    @Schema(description = "Type of token (token_type)", example = "Bearer", allowableValues = {"Bearer"}, name = "token_type")
    @JsonProperty("token_type")
    String tokenType,
    
    @Schema(description = "Access token expiration time in seconds (expires_in)", example = "900", name = "expires_in")
    @JsonProperty("expires_in")
    Long expiresIn,
    
    @Schema(description = "User summary information (user)", name = "user")
    @JsonProperty("user")
    UserSummaryResponse user
) {
    /**
     * Compact constructor to set default token type.
     */
    public AuthResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }
    
    /**
     * Static factory method to create AuthResponse with tokens and user information.
     * This provides a clean way to create authentication responses.
     * 
     * @param accessToken JWT access token
     * @param refreshToken Refresh token
     * @param expiresIn Token expiration time in seconds
     * @param user User summary information
     * @return AuthResponse DTO
     */
    public static AuthResponse create(String accessToken, String refreshToken, Long expiresIn, UserSummaryResponse user) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}
