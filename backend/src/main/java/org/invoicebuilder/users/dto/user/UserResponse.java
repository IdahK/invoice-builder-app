package org.invoicebuilder.users.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.invoicebuilder.users.domain.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Complete user response with all user details and metadata.
 */
@Schema(description = "Complete user response with all user details and metadata")
public record UserResponse(
    
    @Schema(description = "Unique identifier for the user (user_id)", example = "550e8400-e29b-41d4-a716-446655440000", name = "user_id")
    @JsonProperty("user_id")
    UUID userId,
    
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
    UserStatus status,
    
    @Schema(description = "Unique identifier for the user's account (account_id)", example = "550e8400-e29b-41d4-a716-446655440001", name = "account_id")
    @JsonProperty("account_id")
    UUID accountId,
    
    @Schema(description = "Name of the user's account (account_name)", example = "John's Account", name = "account_name")
    @JsonProperty("account_name")
    String accountName,

    @Schema(description = "List of roles assigned to the user (roles)", example = "[\"USER\", \"ADMIN\"]", name = "roles")
    @JsonProperty("roles")
    List<String> roles,
    
    @Schema(description = "Timestamp when the user was created (created_at)", example = "2024-01-15T10:30:00Z", name = "created_at")
    @JsonProperty("created_at")
    Instant createdAt,
    
    @Schema(description = "Timestamp when the user was last updated (updated_at)", example = "2024-01-20T14:25:00Z", name = "updated_at")
    @JsonProperty("updated_at")
    Instant updatedAt
) {
    
    /**
     * Static factory method to create UserResponse from a User domain entity.
     * This provides a clean way to convert from domain model to DTO.
     * 
     * @param user The User domain entity
     * @param roleNames List of role names for the user
     * @return UserResponse DTO
     */
    public static UserResponse from(org.invoicebuilder.users.domain.User user, List<String> roleNames) {
        return new UserResponse(
            user.getUserId(),
            user.getUserEmail(),
            user.getUserDisplayName(),
            user.getUserEmailVerified(),
            user.getUserStatus(),
            user.getAccount().getAccountId(),
            user.getAccount().getAccountName(),
            roleNames,
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
