package org.invoicebuilder.users.service.registration.interfaces;

import jakarta.validation.constraints.NotNull;
import org.invoicebuilder.users.dto.UserRegisterRequest;

/**
 * Interface for registration validation operations
 * Enables Liskov Substitution Principle
 */
public interface IRegistrationValidationService {
    
    /**
     * Validates the registration request parameters
     */
    void validateRegistrationRequest(@NotNull UserRegisterRequest request);
    
    /**
     * Validates email format using basic validation
     */
    void validateEmailFormat(String email);
    
    /**
     * Validates password strength
     */
    void validatePasswordStrength(String password);
}
