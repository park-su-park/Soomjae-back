package com.parksupark.soomjae.server.member.dto;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private String email;
    private String password;
}
