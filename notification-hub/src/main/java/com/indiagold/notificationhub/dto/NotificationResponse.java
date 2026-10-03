package com.indiagold.notificationhub.dto;

import java.util.List;

public class NotificationResponse {

    private Long userId;
    private List<ChannelResult> results;

    public NotificationResponse(Long userId, List<ChannelResult> results) {
        this.userId = userId;
        this.results = results;
    }

    public Long getUserId() {
        return userId;
    }

    public List<ChannelResult> getResults() {
        return results;
    }
}
