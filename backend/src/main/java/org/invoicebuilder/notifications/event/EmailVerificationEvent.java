package org.invoicebuilder.notifications.event;

import lombok.Builder;
import lombok.Data;

/**
 * Event for email verification notifications.
 */
@Data
@Builder
public class EmailVerificationEvent implements EmailEvent {
    private String userEmail;
    private String verificationToken;
    private String userName;
    private String message;
    
    @Override
    public String getEventType() {
        return "EMAIL_VERIFICATION";
    }
}
