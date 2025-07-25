package com.parksupark.soomjae.server.member.dto;

import static com.parksupark.soomjae.server.common.constant.ValidationMessages.NOT_BLANK;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PatchNicknameRequest {

    @NotBlank(message = NOT_BLANK)
    private final String nickname;

    @JsonCreator
    public PatchNicknameRequest(
        @JsonProperty("nickname") String nickname
    ) {
        this.nickname = nickname;
    }
}
