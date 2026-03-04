package org.invoicebuilder.users.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * User summary response with essential user information.
 * Used for authentication responses and user listings.
 */
@Schema(description = "User summary response with essential user information. Used for authentication responses and user listings.")
@Builder
public record UserSummaryResponse(
    
    @Schema(description = "Unique identifier for the user (user_id)", example = "550e8400-e29b-41d4-a716-446655440000", name = "user_id")
    @JsonProperty("user_id")
    UUID userId,
    
    @Schema(description = "Unique identifier for the user's account (account_id)", example = "550e8400-e29b-41d4-a716-446655440001", name = "account_id")
    @JsonProperty("account_id")
    UUID accountId,
    
    @Schema(description = "User's email address (email_address)", example = "user@example.com", name = "email_address")
    @JsonProperty("email_address")
    String email,
    
    @Schema(description = "User's display name (display_name)", example = "John Doe", name = "display_name")
    @JsonProperty("display_name")
    String displayName,
    
    @Schema(description = "Whether the user's email has been verified (email_verified)", example = "true", name = "email_verified")
    @JsonProperty("email_verified")
    Boolean emailVerified,
    
    @Schema(description = "Current status of the user account (status)", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"}, name = "status")
    @JsonProperty("status")
    String status,
    
    @Schema(description = "List of roles assigned to the user (roles)", example = "[\"USER\", \"ADMIN\"]", name = "roles")
    @JsonProperty("roles")
    List<String> roles
) {
    
    /**
     * Static factory method to create UserSummaryResponse from a User domain entity.
     * This provides a clean way to convert from domain model to DTO.
     * 
     * @param user The User domain entity
     * @param roleNames List of role names for the user
     * @return UserSummaryResponse DTO
     */
    public static UserSummaryResponse from(org.invoicebuilder.users.domain.User user, List<String> roleNames) {
        return new UserSummaryResponse(
            user.getUserId(),
            user.getAccount().getAccountId(),
            user.getUserEmail(),
            user.getUserDisplayName(),
            user.getUserEmailVerified(),
            user.getUserStatus().name(),
            roleNames
        );
    }
}
