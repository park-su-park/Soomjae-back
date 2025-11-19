package com.parksupark.soomjae.server.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMTokenRequest {

    private String fcmToken;
    private String device;
}
