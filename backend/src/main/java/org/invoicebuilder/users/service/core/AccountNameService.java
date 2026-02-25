package org.invoicebuilder.users.service.core;

import org.springframework.stereotype.Service;

/**
 * Service responsible for account name generation and validation
 * Follows Single Responsibility Principle
 */
@Service
public class AccountNameService {

    /**
     * Generates or validates account name based on user input
     * For personal users (no account name provided), generates from email
     * For business users, validates the provided account name
     * 
     * @param accountName Optional account name from request
     * @param email User's email address
     * @param displayName User's display name
     * @return Validated or generated account name
     * @throws IllegalArgumentException if validation fails
     */
    public String generateOrValidateAccountName(String accountName, String email, String displayName) {
        if (accountName == null || accountName.trim().isEmpty()) {
            // Generate account name for personal users
            return generatePersonalAccountName(email, displayName);
        } else {
            // Validate business account name
            return validateBusinessAccountName(accountName);
        }
    }

    /**
     * Generates account name for personal users based on email
     * 
     * @param email User's email address
     * @param displayName User's display name
     * @return Generated account name
     */
    String generatePersonalAccountName(String email, String displayName) {
        // Extract username from email (before @)
        String username = email.split("@")[0];
        
        // Clean up the username
        username = username.replaceAll("[^a-zA-Z0-9]", "");
        
        // If username is too short or empty, use display name
        if (username.length() < 3) {
            username = displayName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        }
        
        // Ensure minimum length
        if (username.length() < 3) {
            username = "user" + System.currentTimeMillis() % 1000;
        }
        
        // Capitalize first letter
        return username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
    }

    /**
     * Validates business account name
     * 
     * @param accountName The account name to validate
     * @return Validated account name
     * @throws IllegalArgumentException if validation fails
     */
    private String validateBusinessAccountName(String accountName) {
        String trimmedName = accountName.trim();
        
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be empty");
        }
        
        if (trimmedName.length() < 2) {
            throw new IllegalArgumentException("Account name must be at least 2 characters long");
        }
        
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("Account name must not exceed 100 characters");
        }
        
        // Check for invalid characters
        if (!trimmedName.matches("^[a-zA-Z0-9\\s\\-_]+$")) {
            throw new IllegalArgumentException("Account name can only contain letters, numbers, spaces, hyphens, and underscores");
        }
        
        return trimmedName;
    }
}
