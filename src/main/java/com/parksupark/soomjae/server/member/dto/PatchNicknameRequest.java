package com.parksupark.soomjae.server.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PatchNicknameRequest {

    private final String nickname;

    @JsonCreator
    public PatchNicknameRequest(
        @JsonProperty("nickname") String nickname
    ) {
        this.nickname = nickname;
    }
}
