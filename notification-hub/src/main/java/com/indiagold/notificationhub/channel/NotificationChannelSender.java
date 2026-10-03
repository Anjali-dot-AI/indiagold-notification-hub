package com.indiagold.notificationhub.channel;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;

/**
 * Strategy interface: every delivery channel (Email/SMS/Push/In-App) implements this
 * same contract. The dispatcher service never needs to know HOW a channel sends a
 * message - it just calls send() and the right implementation handles the details.
 *
 * This is what makes the system open to extension: adding a 5th channel (say,
 * WhatsApp) later means writing ONE new class implementing this interface -
 * zero changes needed in the dispatcher or controller.
 */
public interface NotificationChannelSender {

    /** Which channel this implementation handles - used by the dispatcher to route correctly. */
    Channel getChannelType();

    /**
     * Attempt to deliver the message. Throws RuntimeException on simulated failure
     * (e.g. missing contact info) so the dispatcher can catch it and log FAILED status.
     */
    void send(User recipient, String title, String body);
}
