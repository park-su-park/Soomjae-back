package com.parksupark.soomjae.server.auth.username.dto;

import lombok.Getter;

@Getter
public class UsernamePasswordAuthSuccessResponse {

    private final String accessToken;
    private final String tokenType = "Bearer";

    public UsernamePasswordAuthSuccessResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
