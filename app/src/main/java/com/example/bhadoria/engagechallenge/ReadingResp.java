package com.example.bhadoria.engagechallenge;

/**
 * Created by bhadoria on 2/25/18.
 */

public class ReadingResp {
    private String deviceId;
    private Long weight;
    private Long createdAt;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
