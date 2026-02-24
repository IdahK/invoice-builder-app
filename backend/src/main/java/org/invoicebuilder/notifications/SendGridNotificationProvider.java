package org.invoicebuilder.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("sendGridProvider")
@RequiredArgsConstructor
@Slf4j
public class SendGridNotificationProvider implements NotificationProvider {
    
    @Value("${notifications.sendgrid.api-key:}")
    private String apiKey;
    
    @Override
    public void sendEmail(String to, String subject, String content) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("SendGrid API key not configured");
        }
        
        log.info("Sending email via SendGrid to {}: {}", to, subject);
        
    }
    
    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }
}
