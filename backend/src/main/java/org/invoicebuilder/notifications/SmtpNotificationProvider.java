package org.invoicebuilder.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("smtpProvider")
@RequiredArgsConstructor
@Slf4j
public class SmtpNotificationProvider implements NotificationProvider {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        
        try {
            mailSender.send(message);
            log.info("Email sent via SMTP to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email via SMTP to {}", to, e);
            throw new RuntimeException("Failed to send email via SMTP");
        }
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
