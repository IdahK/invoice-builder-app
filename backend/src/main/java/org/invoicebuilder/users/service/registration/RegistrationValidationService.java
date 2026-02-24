package org.invoicebuilder.users.service.registration;

import org.invoicebuilder.users.dto.UserRegisterRequest;
import org.springframework.stereotype.Service;

/**
 * Service responsible for registration validation
 * Follows Single Responsibility Principle
 */
@Service
public class RegistrationValidationService {

    /**
     * Validates the complete registration request
     * 
     * @param request The registration request to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateRegistrationRequest(UserRegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }
        
        // Additional validation can be added here
        validateEmail(request.email());
        validatePassword(request.password());
        validateDisplayName(request.displayName());
    }

    /**
     * Validates email format
     * 
     * @param email The email to validate
     * @throws IllegalArgumentException if email is invalid
     */
    public void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Validates password strength
     * 
     * @param password The password to validate
     * @throws IllegalArgumentException if password is weak
     */
    public void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        if (password.length() > 128) {
            throw new IllegalArgumentException("Password must not exceed 128 characters");
        }
        
        // Add more password strength validation as needed
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
    }

    /**
     * Validates display name
     * 
     * @param displayName The display name to validate
     * @throws IllegalArgumentException if display name is invalid
     */
    private void validateDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name is required");
        }
        
        if (displayName.length() > 100) {
            throw new IllegalArgumentException("Display name must not exceed 100 characters");
        }
    }

    /**
     * Validates email (basic validation)
     * 
     * @param email The email to validate
     * @throws IllegalArgumentException if email is invalid
     */
    private void validateEmail(String email) {
        validateEmailFormat(email);
    }

    /**
     * Validates password (basic validation)
     * 
     * @param password The password to validate
     * @throws IllegalArgumentException if password is invalid
     */
    private void validatePassword(String password) {
        validatePasswordStrength(password);
    }
}
