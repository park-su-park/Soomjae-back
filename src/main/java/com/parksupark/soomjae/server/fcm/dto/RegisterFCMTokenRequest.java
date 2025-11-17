package com.parksupark.soomjae.server.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterFCMTokenRequest {

    private final String fcmToken;
}
