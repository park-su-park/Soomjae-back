package com.parksupark.soomjae.server.member.dto;

import lombok.Data;

@Data
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String nickname;
}
