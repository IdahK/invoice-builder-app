package org.invoicebuilder.users.service.registration.interfaces;

import org.invoicebuilder.users.dto.UserRegisterRequest;
import org.invoicebuilder.users.dto.UserRegistrationResponse;

/**
 * Strategy interface for different registration flows
 * Enables flexible registration processes (Standard, OAuth, Admin-created, etc.)
 */
public interface RegistrationStrategy {
    
    /**
     * Executes the registration process using the specific strategy
     * 
     * @param request The registration request containing user data
     * @return RegistrationResponse with created user and account details
     * @throws IllegalArgumentException if validation fails
     * @throws RuntimeException if registration fails
     */
    UserRegistrationResponse register(UserRegisterRequest request);
    
    /**
     * Returns the strategy type identifier
     * Used for strategy selection and logging
     * 
     * @return String identifier for this registration strategy
     */
    String getStrategyType();
}
