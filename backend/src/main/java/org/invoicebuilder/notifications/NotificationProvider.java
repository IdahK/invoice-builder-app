package org.invoicebuilder.notifications;

public interface NotificationProvider {
    
    void sendEmail(String to, String subject, String content);
    
    boolean isAvailable();
}
