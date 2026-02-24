package org.invoicebuilder.users.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountNameValidatorTest {

    private AccountNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AccountNameValidator();
    }

    @Test
    void shouldAcceptNullAccountName() {
        // Personal user - no account name provided
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void shouldAcceptEmptyAccountName() {
        // Personal user - empty account name
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @Test
    void shouldAcceptValidBusinessAccountNames() {
        // Business user - valid account names
        assertTrue(validator.isValid("Acme Corporation", null));
        assertTrue(validator.isValid("AB", null)); // Minimum length
        assertTrue(validator.isValid("A".repeat(100), null)); // Maximum length
    }

    @Test
    void shouldRejectInvalidBusinessAccountNames() {
        // Business user - invalid account names
        assertFalse(validator.isValid("A", null)); // Too short
        assertFalse(validator.isValid("A".repeat(101), null)); // Too long
        assertFalse(validator.isValid("  A  ", null)); // Too short after trim
    }

    @Test
    void shouldHandleWhitespaceProperly() {
        // Should trim before validation
        assertTrue(validator.isValid("  Valid Name  ", null));
        assertFalse(validator.isValid("  A  ", null)); // Still too short after trim
    }
}
