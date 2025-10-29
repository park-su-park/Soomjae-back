package com.parksupark.soomjae.server.member.dto;

import static com.parksupark.soomjae.server.common.constant.ValidationMessages.EMAIL_INVALID_FORMAT;
import static com.parksupark.soomjae.server.common.constant.ValidationMessages.NOT_BLANK;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateMemberRequest {

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_INVALID_FORMAT)
    private final String email;

    // 비밀번호 패턴 정책 확립후 Validation 적용
    @NotBlank(message = NOT_BLANK)
    private final String password;

    @JsonCreator
    public CreateMemberRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password
    ) {
        this.email = email;
        this.password = password;
    }
}
