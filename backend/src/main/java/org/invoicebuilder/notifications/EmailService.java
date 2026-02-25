package org.invoicebuilder.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invoicebuilder.notifications.event.EmailEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendEmailEvent(EmailEvent emailEvent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailEvent.getUserEmail());
        message.setSubject(getSubjectForEventType(emailEvent.getEventType()));
        message.setText(emailEvent.getMessage());
        
        try {
            javaMailSender.send(message);
            log.info("{} email sent to {}", emailEvent.getEventType(), emailEvent.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send {} email to {}", emailEvent.getEventType(), emailEvent.getUserEmail(), e);
            throw new RuntimeException("Failed to send " + emailEvent.getEventType() + " email");
        }
    }
    
    private String getSubjectForEventType(String eventType) {
        return switch (eventType) {
            case "EMAIL_VERIFICATION" -> "Verify your email address";
            case "PASSWORD_RESET" -> "Reset your password";
            default -> "Invoice Builder Notification";
        };
    }
}
