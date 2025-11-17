package com.parksupark.soomjae.server.profile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    private final String bio;
    private final String profileImageUrl;

    @JsonCreator
    public UpdateProfileRequest(
        @JsonProperty String bio,
        @JsonProperty String profileImageUrl
    ) {
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
}
