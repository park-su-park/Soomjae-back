package com.parksupark.soomjae.server.profile.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileResponse {

    private final Long memberId;
    private final Long profileId;
    private final String bio;
    private final String profileImageUrl;
    private final String nickname;
}
