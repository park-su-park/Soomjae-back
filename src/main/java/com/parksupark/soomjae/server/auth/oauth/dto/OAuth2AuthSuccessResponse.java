package com.parksupark.soomjae.server.auth.oauth.dto;

import lombok.Getter;

@Getter
public class OAuth2AuthSuccessResponse {

    private final String accessToken;
    private final Long memberId;
    private final String tokenType;

    public OAuth2AuthSuccessResponse() {
        this.accessToken = null;
        this.memberId = null;
        this.tokenType = null;
    }

    public OAuth2AuthSuccessResponse(String accessToken, Long memberId) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.tokenType = "Bearer";
    }
}
