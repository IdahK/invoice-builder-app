package org.invoicebuilder.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false, updatable = false)
    private UUID accountId;
    
    @Column(name = "account_name", nullable = false)
    private String accountName;
    
    @Column(name = "account_active", nullable = false)
    @Builder.Default
    private Boolean accountActive = true;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "account_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
