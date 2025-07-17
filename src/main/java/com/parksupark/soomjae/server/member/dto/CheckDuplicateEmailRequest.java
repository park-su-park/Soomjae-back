package com.parksupark.soomjae.server.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parksupark.soomjae.server.common.constant.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckDuplicateEmailRequest {

    @Email(message = ValidationMessages.EMAIL_INVALID_FORMAT)
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private final String email;

    @JsonCreator
    public CheckDuplicateEmailRequest(
            @JsonProperty("email") String email
    ) {
        this.email = email;
    }
}
