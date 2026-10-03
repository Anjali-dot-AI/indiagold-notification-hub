package com.indiagold.notificationhub.channel;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import org.springframework.stereotype.Component;

@Component
public class SmsChannelSender implements NotificationChannelSender {

    @Override
    public Channel getChannelType() {
        return Channel.SMS;
    }

    @Override
    public void send(User recipient, String title, String body) {
        if (recipient.getPhoneNumber() == null || recipient.getPhoneNumber().isBlank()) {
            throw new RuntimeException("User has no phone number on file");
        }
        // Mock dispatch - in a real system this would call an SMS gateway (Twilio, MSG91, etc.)
        System.out.println("[SMS] To: " + recipient.getPhoneNumber() + " | " + title + ": " + body);
    }
}
