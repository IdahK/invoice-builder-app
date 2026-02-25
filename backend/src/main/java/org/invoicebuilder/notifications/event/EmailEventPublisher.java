package org.invoicebuilder.notifications.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Generic email event publisher that can publish any type of email event.
 * This provides a reusable way to send different types of email notifications
 * such as email verification, password reset, account notifications, etc.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Publishes a email event.
     * 
     * @param event The email event to publish
     */
    public void publishEmailEvent(EmailEvent event) {
        log.info("Publishing email event: {} for user: {}", 
                event.getEventType(), 
                event.getUserEmail());
        eventPublisher.publishEvent(event);
    }
}
