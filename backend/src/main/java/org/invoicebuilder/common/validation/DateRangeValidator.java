package org.invoicebuilder.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    
    private String issueDateField;
    private String dueDateField;
    
    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.issueDateField = constraintAnnotation.issueDateField();
        this.dueDateField = constraintAnnotation.dueDateField();
    }
    
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field issueDateFieldObj = object.getClass().getDeclaredField(issueDateField);
            Field dueDateFieldObj = object.getClass().getDeclaredField(dueDateField);
            
            issueDateFieldObj.setAccessible(true);
            dueDateFieldObj.setAccessible(true);
            
            LocalDate issueDate = (LocalDate) issueDateFieldObj.get(object);
            LocalDate dueDate = (LocalDate) dueDateFieldObj.get(object);
            
            if (issueDate == null || dueDate == null) {
                return true; // Let @NotNull handle null validation
            }
            
            // Check if due date is before issue date
            if (dueDate.isBefore(issueDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Due date {0} cannot be before issue date {1}")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            // If reflection fails, return true to avoid breaking validation
            return true;
        }
    }
}
