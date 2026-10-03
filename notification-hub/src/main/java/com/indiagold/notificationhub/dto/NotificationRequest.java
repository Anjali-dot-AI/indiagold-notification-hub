package com.indiagold.notificationhub.dto;

import com.indiagold.notificationhub.model.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * This is the single generic payload shape the unified endpoint accepts,
 * regardless of which channels are being targeted.
 */
public class NotificationRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "body is required")
    private String body;

    @NotEmpty(message = "channels list cannot be empty")
    private List<Channel> channels;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
