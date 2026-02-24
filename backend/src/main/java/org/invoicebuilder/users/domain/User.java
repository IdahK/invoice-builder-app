package org.invoicebuilder.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.invoicebuilder.security.domain.EmailVerificationToken;
import org.invoicebuilder.security.domain.OAuthAccount;
import org.invoicebuilder.security.domain.RefreshToken;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "public", indexes = {
    @Index(name = "idx_user_email", columnList = "user_email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;
    
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;
    
    @Column(name = "user_display_name")
    private String userDisplayName;
    
    @Column(name = "user_password_hash")
    private String userPassword;
    
    @Column(name = "user_email_verified", nullable = false)
    @Builder.Default
    private Boolean userEmailVerified = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_account"))
    private Account account;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_role_user")),
        inverseJoinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_user_role_role"))
    )
    private Set<Role> roles = new HashSet<>();
    
    // Proper setter for roles to handle lazy loading
    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? roles : new HashSet<>();
    }
    
    // Proper getter for roles to handle lazy loading
    public Set<Role> getRoles() {
        return roles != null ? roles : new HashSet<>();
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OAuthAccount> oauthAccounts = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailVerificationToken> emailVerificationTokens = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "user_created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "user_updated_at", nullable = false)
    private Instant  updatedAt;
}
