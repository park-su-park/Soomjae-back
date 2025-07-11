package com.parksupark.soomjae.server.member.dto;

import lombok.Getter;

@Getter
public class CheckDuplicateEmailResponse {

    private final boolean duplicate;

    public CheckDuplicateEmailResponse(boolean availability) {
        this.duplicate = availability;
    }
}
