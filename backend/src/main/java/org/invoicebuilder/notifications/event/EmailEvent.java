package org.invoicebuilder.notifications.event;

/**
 * Base interface for all email events.
 * This provides a common contract for different types of email notifications
 * and allows the EmailEventPublisher to handle them generically.
 */
public interface EmailEvent {
    
    /**
     * Gets the type of email event for logging and routing purposes.
     * 
     * @return The event type (e.g., "EMAIL_VERIFICATION", "PASSWORD_RESET")
     */
    String getEventType();
    
    /**
     * Gets the target user's email address.
     * 
     * @return The user's email address
     */
    String getUserEmail();
    
    /**
     * Gets the user's display name for personalization.
     * 
     * @return The user's display name
     */
    String getUserName();
    
    /**
     * Gets the email message content to be sent.
     * This can be HTML or plain text depending on the email service implementation.
     * 
     * @return The email message content
     */
    String getMessage();
}
