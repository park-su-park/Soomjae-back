package com.parksupark.soomjae.server.member.dto;

import static com.parksupark.soomjae.server.common.constant.ValidationMessages.EMAIL_INVALID_FORMAT;
import static com.parksupark.soomjae.server.common.constant.ValidationMessages.NOT_BLANK;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PatchEmailRequest {

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_INVALID_FORMAT)
    private final String email;

    @JsonCreator
    public PatchEmailRequest(
        @JsonProperty("email") String email
    ) {
        this.email = email;
    }
}
