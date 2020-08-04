package com.quintus.labs.datingapp.rest.RequestModel;

public class FcmTokenUpdateRequest {
     private String fcmToken;

    public FcmTokenUpdateRequest(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
