package com.parksupark.soomjae.server.profile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @Nullable
    private final String bio;

    @Nullable
    private final String profileImageUrl;

    @Nullable
    private final String nickname;

    @JsonCreator
    public UpdateProfileRequest(
        @JsonProperty("bio") String bio,
        @JsonProperty("profileImageUrl") String profileImageUrl,
        @JsonProperty("nickname") String nickname
    ) {
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }
}
