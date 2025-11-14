package com.parksupark.soomjae.server.profile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateProfileRequest {

    private final String bio;
    private final String profileImageUrl;

    @JsonCreator
    public CreateProfileRequest(
        @JsonProperty("bio") String bio,
        @JsonProperty("profileImageUrl") String profileImageUrl
    ) {
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
}
