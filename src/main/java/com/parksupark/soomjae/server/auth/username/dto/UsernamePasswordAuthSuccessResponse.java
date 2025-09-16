package com.parksupark.soomjae.server.auth.username.dto;

import lombok.Getter;

@Getter
public class UsernamePasswordAuthSuccessResponse {

    private final String accessToken;
    private final String tokenType;
    private final Long memberId;

    public UsernamePasswordAuthSuccessResponse() {
        this.accessToken = null;
        this.tokenType = null;
        this.memberId = null;
    }

    public UsernamePasswordAuthSuccessResponse(String accessToken, Long memberId) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.memberId = memberId;
    }
}
