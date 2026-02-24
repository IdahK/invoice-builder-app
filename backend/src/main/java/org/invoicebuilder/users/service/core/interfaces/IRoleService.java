package org.invoicebuilder.users.service.core.interfaces;

import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.domain.Role;

/**
 * Interface for role management operations
 * Enables Liskov Substitution Principle
 */
public interface IRoleService {
    
    /**
     * Assigns the OWNER role to a user
     */
    void assignOwnerRole(User user);
    
    /**
     * Finds a role by name
     */
    Role findRoleByName(String roleName);
}
