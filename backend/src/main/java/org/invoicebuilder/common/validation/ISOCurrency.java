package org.invoicebuilder.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISOCurrencyValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ISOCurrency {
    String message() default "Invalid currency code. Must be a valid ISO-4217 currency code";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
