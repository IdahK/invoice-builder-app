package org.invoicebuilder.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    @Column(name = "role_id", columnDefinition = "UUID")
    private UUID roleId;
    
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
}
