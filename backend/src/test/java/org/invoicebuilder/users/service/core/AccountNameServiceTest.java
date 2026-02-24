package org.invoicebuilder.users.service.core;

import org.invoicebuilder.users.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountNameServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountNameService accountNameService;

    @Test
    void shouldReturnTrimmedBusinessAccountName() {
        // Given
        String businessName = "  Acme Corporation  ";
        
        // When
        String result = accountNameService.generateOrValidateAccountName(businessName, "john@example.com", "John");
        
        // Then
        assertEquals("Acme Corporation", result);
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldGeneratePersonalAccountNameFromDisplayName() {
        // Given
        String displayName = "John Doe";
        
        // When
        String result = accountNameService.generatePersonalAccountName("john@example.com", displayName);
        
        // Then
        assertTrue(result.startsWith("JohnDoe-Personal-"));
        assertTrue(result.matches("JohnDoe-Personal-\\d{4}")); // 4 digits timestamp
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldGeneratePersonalAccountNameFromEmail() {
        // Given
        String email = "jane.smith@example.com";
        
        // When
        String result = accountNameService.generatePersonalAccountName(email, null);
        
        // Then
        assertTrue(result.startsWith("janesmith-Personal-"));
        assertTrue(result.matches("janesmith-Personal-\\d{4}")); // 4 digits timestamp
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldGenerateUniqueNamesForMultipleCalls() throws InterruptedException {
        // Given
        String email = "john@example.com";
        
        // When
        String result1 = accountNameService.generatePersonalAccountName(email, null);
        Thread.sleep(1); // Ensure different timestamp
        String result2 = accountNameService.generatePersonalAccountName(email, null);
        
        // Then
        assertNotEquals(result1, result2);
        assertTrue(result1.matches("john-Personal-\\d{4}"));
        assertTrue(result2.matches("john-Personal-\\d{4}"));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldHandleComplexEmailAddresses() {
        // Given
        String email = "john.doe.jr+test@company-domain.com";
        
        // When
        String result = accountNameService.generatePersonalAccountName(email, null);
        
        // Then
        assertTrue(result.startsWith("johndoejrtest-Personal-"));
        assertTrue(result.matches("johndoejrtest-Personal-\\d{4}"));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldPreferDisplayNameOverEmail() {
        // Given
        String displayName = "Jane Smith";
        String email = "different.email@example.com";
        
        // When
        String result = accountNameService.generatePersonalAccountName(email, displayName);
        
        // Then
        assertTrue(result.startsWith("JaneSmith-Personal-"));
        assertTrue(result.matches("JaneSmith-Personal-\\d{4}"));
        verifyNoInteractions(accountRepository);
    }
}
