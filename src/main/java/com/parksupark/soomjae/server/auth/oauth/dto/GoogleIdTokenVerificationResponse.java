package com.parksupark.soomjae.server.auth.oauth.dto;

import lombok.Getter;

@Getter
public class GoogleIdTokenVerificationResponse {

    private final String accessToken;
    private final String tokenType;
    private final Long memberId;

    public GoogleIdTokenVerificationResponse() {
        this.accessToken = null;
        this.tokenType = null;
        this.memberId = null;
    }

    public GoogleIdTokenVerificationResponse(String accessToken, Long memberId) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.memberId = memberId;
    }
}