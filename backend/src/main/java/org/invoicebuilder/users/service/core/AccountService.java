package org.invoicebuilder.users.service.core;

import org.invoicebuilder.users.domain.Account;
import org.invoicebuilder.users.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service responsible for account management operations
 * Follows Single Responsibility Principle
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a new account with the given name
     * 
     * @param accountName The name of the account to create
     * @return The created account
     */
    @Transactional
    public Account createAccount(String accountName) {
        Account account = Account.builder()
                .accountName(accountName)
                .accountActive(true)
                .build();

        return accountRepository.save(account);
    }

    /**
     * Finds an account by its ID
     * 
     * @param accountId The UUID of the account
     * @return The account if found
     */
    public Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new org.invoicebuilder.exception.common.ResourceNotFoundException(
                        "Account", "accountId", accountId.toString()));
    }

    /**
     * Updates an existing account
     * 
     * @param account The account to update
     * @return The updated account
     */
    @Transactional
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Deactivates an account
     * 
     * @param accountId The UUID of the account to deactivate
     */
    @Transactional
    public void deactivateAccount(UUID accountId) {
        Account account = findAccountById(accountId);
        account.setAccountActive(false);
        accountRepository.save(account);
    }
}
