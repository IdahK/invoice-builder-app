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
@Table(name = "oauth_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "oauth_account_id")
    private UUID oauthAccountId;
    
    @Column(name = "oauth_provider", nullable = false)
    private String provider;
    
    @Column(name = "oauth_provider_user_id", nullable = false)
    private String providerUserId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_oauth_account_user"))
    private User user;
    
    @CreationTimestamp
    @Column(name = "oauth_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
