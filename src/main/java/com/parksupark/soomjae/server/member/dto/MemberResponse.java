package com.parksupark.soomjae.server.member.dto;

import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String nickname;

    public MemberResponse(Long memberId, String email, String nickname) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
    }
}
