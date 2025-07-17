package com.parksupark.soomjae.server.auth.username.dto;

import lombok.Getter;

@Getter
public class UsernamePasswordAuthSuccessResponse {

    private final String accessToken;
    private static final String TOKEN_TYPE = "Bearer";
    private final Long memberId;

    public UsernamePasswordAuthSuccessResponse(String accessToken, Long memberId) {
        this.accessToken = accessToken;
        this.memberId = memberId;
    }
}
