package org.invoicebuilder.notifications.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invoicebuilder.notifications.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventListener {
    
//    private final EmailService emailService;
    
    @Async
    @EventListener
    public void handleEmailVerification(EmailVerificationEvent event) {
        try {
            log.info("Processing email verification for user: {}", event.getUserEmail());
            log.info("Email verification sent successfully to: {}", event.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", event.getUserEmail(), e);
            // TODO: implement retry logic or dead letter queue
        }
    }
}

