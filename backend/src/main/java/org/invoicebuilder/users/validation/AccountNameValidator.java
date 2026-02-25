package org.invoicebuilder.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountNameValidator implements ConstraintValidator<ValidAccountName, String> {

    @Override
    public boolean isValid(String accountName, ConstraintValidatorContext context) {
        // Null or empty values are valid (personal users)
        if (accountName == null || accountName.trim().isEmpty()) {
            return true;
        }
        
        // Validate size for business users
        String trimmedName = accountName.trim();
        return trimmedName.length() >= 2 && trimmedName.length() <= 100;
    }
}
