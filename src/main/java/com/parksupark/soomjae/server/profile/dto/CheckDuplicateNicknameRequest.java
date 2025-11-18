package com.parksupark.soomjae.server.profile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CheckDuplicateNicknameRequest {

    private final String nickname;

    @JsonCreator
    public CheckDuplicateNicknameRequest(
        @JsonProperty String nickname
    ) {
        this.nickname = nickname;
    }


}
