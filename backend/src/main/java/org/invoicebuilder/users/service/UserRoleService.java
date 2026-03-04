package org.invoicebuilder.users.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.users.domain.Role;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.domain.UserRole;
import org.invoicebuilder.users.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing user-role relationships
 * Uses dedicated UserRole entity with EmbeddedId for explicit control
 */
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    /**
     * Assigns a role to a user using dedicated UserRole entity
     */
    @Transactional
    public User assignRoleToUser(User user, String roleName) {
        Role role = roleService.findRoleByName(roleName);
        
        // Check if role assignment already exists
        if (userRoleRepository.existsById_UserIdAndId_RoleId(user.getUserId(), role.getRoleId())) {
            return user; // Role already assigned
        }

        // Create new UserRole assignment
        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
        
        return user;
    }

    /**
     * Assigns multiple roles to a user
     */
    @Transactional
    public User assignMultipleRoles(User user, List<String> roleNames) {
        for (String roleName : roleNames) {
            assignRoleToUser(user, roleName);
        }
        return user;
    }

    /**
     * Removes a role from a user
     */
    @Transactional
    public User removeRoleFromUser(User user, String roleName) {
        Role role = roleService.findRoleByName(roleName);
        
        if (userRoleRepository.existsById_UserIdAndId_RoleId(user.getUserId(), role.getRoleId())) {
            userRoleRepository.deleteById_UserIdAndId_RoleId(user.getUserId(), role.getRoleId());
        }
        
        return user;
    }

    /**
     * Removes all roles from a user
     */
    @Transactional
    public User removeAllRoles(User user) {
        List<UserRole> userRoles = userRoleRepository.findByUser_UserId(user.getUserId());
        userRoleRepository.deleteAll(userRoles);
        return user;
    }

    /**
     * Checks if user has a specific role
     */
    public boolean userHasRole(User user, String roleName) {
        Role role = roleService.findRoleByName(roleName);
        return userRoleRepository.existsById_UserIdAndId_RoleId(user.getUserId(), role.getRoleId());
    }

    /**
     * Gets all role names for a user
     */
    public List<String> getUserRoleNames(UUID userId) {
        return userRoleRepository.findRoleNamesByUserId(userId);
    }

    /**
     * Assigns OWNER role to a user (convenience method)
     */
    @Transactional
    public User assignOwnerRole(User user) {
        return assignRoleToUser(user, "OWNER");
    }

    /**
     * Assigns ADMIN role to a user (convenience method)
     */
    @Transactional
    public User assignAdminRole(User user) {
        return assignRoleToUser(user, "ADMIN");
    }

    /**
     * Assigns USER role to a user (convenience method)
     */
    @Transactional
    public User assignUserRole(User user) {
        return assignRoleToUser(user, "USER");
    }
}
