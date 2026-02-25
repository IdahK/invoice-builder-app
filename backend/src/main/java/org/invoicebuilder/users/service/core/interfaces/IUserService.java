package org.invoicebuilder.users.service.core.interfaces;

import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.dto.UserDtos.CreateUserRequest;

/**
 * Interface for user creation and management operations
 * Enables Liskov Substitution Principle
 */
public interface IUserService {
    
    /**
     * Creates a new user with the given parameters
     */
    User createUser(CreateUserRequest createUserRequest);
    
    /**
     * Checks if a user with the given email already exists
     */
    boolean userExistsByEmail(String email);
    
    /**
     * Updates an existing user
     */
    User updateUser(User user);
    
    /**
     * Retrieves a user by email
     */
    User getUserByEmail(String email);
}
