package com.indiagold.notificationhub.dto;

import com.indiagold.notificationhub.model.Channel;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class UserRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String email;
    private String phoneNumber;
    private String deviceToken;
    private Set<Channel> optedInChannels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Set<Channel> getOptedInChannels() {
        return optedInChannels;
    }

    public void setOptedInChannels(Set<Channel> optedInChannels) {
        this.optedInChannels = optedInChannels;
    }
}
