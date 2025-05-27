package com.parksupark.soomjae.server.member.dto;

import lombok.Data;

@Data
public class CreateMemberResponse {
    private final Long id;
    private final String email;
}
