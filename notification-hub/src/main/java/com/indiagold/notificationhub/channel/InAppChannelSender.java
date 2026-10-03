package com.indiagold.notificationhub.channel;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import org.springframework.stereotype.Component;

@Component
public class InAppChannelSender implements NotificationChannelSender {

    @Override
    public Channel getChannelType() {
        return Channel.IN_APP;
    }

    @Override
    public void send(User recipient, String title, String body) {
        // In-app notifications have no external dependency (no email/phone/device needed) -
        // they'd normally just be written to a "notifications" table the app's UI reads from.
        System.out.println("[IN_APP] Stored for userId: " + recipient.getId() + " | " + title + ": " + body);
    }
}
