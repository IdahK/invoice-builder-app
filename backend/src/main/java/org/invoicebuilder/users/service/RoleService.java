package org.invoicebuilder.users.service;

import org.invoicebuilder.users.domain.Role;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.repository.RoleRepository;
import org.invoicebuilder.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for role management operations
 * Follows Single Responsibility Principle and implements IRoleService
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new org.invoicebuilder.exception.common.ResourceNotFoundException(
                        "Role", "role", roleName));
    }
}
