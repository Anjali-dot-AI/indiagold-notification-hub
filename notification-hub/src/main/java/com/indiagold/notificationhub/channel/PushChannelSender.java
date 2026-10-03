package com.indiagold.notificationhub.channel;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import org.springframework.stereotype.Component;

@Component
public class PushChannelSender implements NotificationChannelSender {

    @Override
    public Channel getChannelType() {
        return Channel.PUSH;
    }

    @Override
    public void send(User recipient, String title, String body) {
        if (recipient.getDeviceToken() == null || recipient.getDeviceToken().isBlank()) {
            throw new RuntimeException("User has no registered device token");
        }
        // Mock dispatch - in a real system this would call FCM/APNs
        System.out.println("[PUSH] Device: " + recipient.getDeviceToken() + " | " + title + ": " + body);
    }
}
