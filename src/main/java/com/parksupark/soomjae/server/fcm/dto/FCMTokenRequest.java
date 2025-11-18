package com.parksupark.soomjae.server.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMTokenRequest {

    private final String fcmToken;
    private final String device;
}
