package org.invoicebuilder.notifications.event;

import lombok.Builder;
import lombok.Data;

/**
 * Event for password reset notifications.
 */
@Data
@Builder
public class PasswordResetEvent implements EmailEvent {
    private String userEmail;
    private String resetToken;
    private String userName;
    private String message;
    
    @Override
    public String getEventType() {
        return "PASSWORD_RESET";
    }
}
