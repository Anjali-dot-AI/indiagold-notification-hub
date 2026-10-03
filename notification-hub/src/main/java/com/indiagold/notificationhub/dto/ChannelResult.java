package com.indiagold.notificationhub.dto;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.DeliveryStatus;

public class ChannelResult {

    private Channel channel;
    private DeliveryStatus status;
    private String detail;

    public ChannelResult(Channel channel, DeliveryStatus status, String detail) {
        this.channel = channel;
        this.status = status;
        this.detail = detail;
    }

    public Channel getChannel() {
        return channel;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }
}
