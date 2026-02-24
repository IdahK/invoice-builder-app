package org.invoicebuilder.security.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.invoicebuilder.users.domain.User;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_verification_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ev_token_id")
    private UUID emailVerificationTokenId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ev_token_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_verification_token_user"))
    private User user;
    
    @Column(name = "ev_token_hash", nullable = false, unique = true)
    private String tokenHash;
    
    @Column(name = "ev_expires_at", nullable = false)
    private Instant expiresAt;
    
    @Column(name = "ev_token_used", nullable = false)
    @Builder.Default
    private Boolean used = false;
    
    @CreationTimestamp
    @Column(name = "ev_token_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
