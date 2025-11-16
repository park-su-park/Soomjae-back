package com.parksupark.soomjae.server.profile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    private final String bio;
    private final String profileImageUrl;
    private final String nickname;

    @JsonCreator
    public UpdateProfileRequest(
        @JsonProperty String bio,
        @JsonProperty String profileImageUrl,
        @JsonProperty String nickname
    ){
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }
}
