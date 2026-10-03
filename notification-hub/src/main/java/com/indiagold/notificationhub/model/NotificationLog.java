package com.indiagold.notificationhub.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long recipientUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    private String messageTitle;

    // populated only when status == FAILED, helps debugging/auditing
    private String failureReason;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public NotificationLog() {
    }

    public NotificationLog(Long recipientUserId, Channel channel, DeliveryStatus status,
                            String messageTitle, String failureReason) {
        this.recipientUserId = recipientUserId;
        this.channel = channel;
        this.status = status;
        this.messageTitle = messageTitle;
        this.failureReason = failureReason;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public Channel getChannel() {
        return channel;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
