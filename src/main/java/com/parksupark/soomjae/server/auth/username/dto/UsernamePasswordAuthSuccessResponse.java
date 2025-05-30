package com.parksupark.soomjae.server.auth.username.dto;

import lombok.Getter;

@Getter
public class UsernamePasswordAuthSuccessResponse {

    private final String accessToken;
    private final String tokenType = "Bearer";
    private final Long memberId;

    public UsernamePasswordAuthSuccessResponse(String accessToken, Long memberId) {
        this.accessToken = accessToken;
        this.memberId = memberId;
    }
}
