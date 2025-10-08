package com.parksupark.soomjae.server.email.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parksupark.soomjae.server.common.constant.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyCodeRequest {

    @Email(message = ValidationMessages.EMAIL_INVALID_FORMAT)
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private final String email;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-z0-9]{6}$", message = "인증코드는 6자리 영문자와 숫자로만 구성되어야 합니다")
    private final String code;

    @JsonCreator
    public VerifyCodeRequest(
        @JsonProperty("email") String email,
        @JsonProperty("code") String code
    ) {
        this.email = email;
        this.code = code;
    }

}
