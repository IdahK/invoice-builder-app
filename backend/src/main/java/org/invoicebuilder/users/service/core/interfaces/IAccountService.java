package org.invoicebuilder.users.service.core.interfaces;

import org.invoicebuilder.users.domain.Account;

/**
 * Interface for account management operations
 * Enables Liskov Substitution Principle
 */
public interface IAccountService {
    
    /**
     * Creates a new account with the given name
     */
    Account createAccount(String accountName);
    
    /**
     * Checks if an account name already exists
     */
    boolean accountNameExists(String accountName);
}
