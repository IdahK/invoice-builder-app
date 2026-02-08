package org.invoicebuilder.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class ISOCurrencyValidator implements ConstraintValidator<ISOCurrency, String> {
    
    private static final Set<String> VALID_CURRENCIES;
    
    static {
        VALID_CURRENCIES = Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext context) {
        if (currencyCode == null) {
            return true; // Let @NotNull handle null validation
        }
        
        boolean isValid = VALID_CURRENCIES.contains(currencyCode.toUpperCase());
        
        if (!isValid) {
            // Provide helpful error message with examples
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Invalid currency code '" + currencyCode + "'. Valid examples: USD, EUR, GBP, KES, JPY")
                    .addConstraintViolation();
        }
        
        return isValid;
    }
}
