package com.indiagold.notificationhub.channel;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import org.springframework.stereotype.Component;

@Component
public class EmailChannelSender implements NotificationChannelSender {

    @Override
    public Channel getChannelType() {
        return Channel.EMAIL;
    }

    @Override
    public void send(User recipient, String title, String body) {
        if (recipient.getEmail() == null || recipient.getEmail().isBlank()) {
            throw new RuntimeException("User has no email address on file");
        }
        // Mock dispatch - in a real system this would call an email provider (SES, SendGrid, etc.)
        System.out.println("[EMAIL] To: " + recipient.getEmail() + " | Subject: " + title + " | Body: " + body);
    }
}
