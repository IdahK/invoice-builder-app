package org.invoicebuilder.users.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountName {
    String message() default "Account name must be between 2 and 100 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
