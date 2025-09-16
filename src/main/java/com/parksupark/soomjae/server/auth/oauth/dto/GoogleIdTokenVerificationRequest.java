package com.parksupark.soomjae.server.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parksupark.soomjae.server.common.constant.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GoogleIdTokenVerificationRequest {

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private final String idToken;

    @JsonCreator
    public GoogleIdTokenVerificationRequest(@JsonProperty("idToken") String idToken) {
        this.idToken = idToken;
    }
}
