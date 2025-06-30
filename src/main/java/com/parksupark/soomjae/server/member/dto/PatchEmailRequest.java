package com.parksupark.soomjae.server.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PatchEmailRequest {

    private final String email;

    @JsonCreator
    public PatchEmailRequest(
        @JsonProperty("email") String email
    ) {
        this.email = email;
    }
}
