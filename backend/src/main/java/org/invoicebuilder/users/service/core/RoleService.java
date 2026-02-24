package org.invoicebuilder.users.service.core;

import org.invoicebuilder.users.domain.Role;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.repository.RoleRepository;
import org.invoicebuilder.users.repository.UserRepository;
import org.invoicebuilder.users.service.core.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for role management operations
 * Follows Single Responsibility Principle and implements IRoleService
 */
@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Assigns a role to a user
     */
    @Transactional
    public User assignRoleToUser(User user, String roleName) {
        Role role = findRoleByName(roleName);
        
        // Initialize roles set if null (lazy loading issue)
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }
        
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignOwnerRole(User user) {
        Role ownerRole = findRoleByName("OWNER");
        
        // Initialize roles set if null (lazy loading issue)
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }
        
        user.getRoles().add(ownerRole);
        userRepository.save(user);
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new org.invoicebuilder.exception.common.ResourceNotFoundException(
                        "Role", "role", roleName));
    }
}
