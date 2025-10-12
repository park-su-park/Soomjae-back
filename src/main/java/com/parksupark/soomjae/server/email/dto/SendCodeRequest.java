package com.parksupark.soomjae.server.email.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SendCodeRequest {

    @Email
    private final String email;

    @JsonCreator
    public SendCodeRequest(
        @JsonProperty("email") String email
    ) {
        this.email = email;
    }

}
