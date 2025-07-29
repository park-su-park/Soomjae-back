package com.parksupark.soomjae.server.auth.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateRefreshTokenRequest {

    private final String subject;
    private final Long memberId;
}
