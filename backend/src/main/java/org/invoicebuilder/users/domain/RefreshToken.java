package org.invoicebuilder.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", indexes = {
    @Index(name = "idx_token_hash", columnList = "refresh_token_hash")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refresh_token_id")
    private UUID refreshTokenId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_refresh_token_user"))
    private User user;
    
    @Column(name = "refresh_token_hash", nullable = false, unique = true)
    private String tokenHash;
    
    @Column(name = "refresh_token_expires_at", nullable = false)
    private Instant expiresAt;
    
    @Column(name = "refresh_token_revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;
    
    @CreationTimestamp
    @Column(name = "refresh_token_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
