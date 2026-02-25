package org.invoicebuilder.notifications;

public final class EmailTemplates {
    
    public static final String EMAIL_VERIFICATION_TEMPLATE = """
            Hello %s,
            
            Please click the following link to verify your email address:
            %s
            
            This link will expire in 24 hours.
            
            If you did not request this verification, please ignore this email.
            
            Best regards,
            Invoice Builder Team
            """;
    
    public static final String PASSWORD_RESET_TEMPLATE = """
            Hello %s,
            
            Please click the following link to reset your password:
            %s
            
            This link will expire in 1 hour.
            
            If you did not request this password reset, please ignore this email.
            
            Best regards,
            Invoice Builder Team
            """;
    
    public static final String WELCOME_TEMPLATE = """
            Welcome to Invoice Builder, %s!
            
            Your account has been successfully created and your email has been verified.
            
            You can now start creating and managing your invoices.
            
            Best regards,
            Invoice Builder Team
            """;
    
    private EmailTemplates() {
        // Utility class
    }
}
