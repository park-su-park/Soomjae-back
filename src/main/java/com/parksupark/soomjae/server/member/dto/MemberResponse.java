package com.parksupark.soomjae.server.member.dto;

import java.time.Instant;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Instant createdTime;
    private final Instant modifiedTime;

    public MemberResponse(Long memberId, String email, String nickname,
                          Instant createdTime, Instant modifiedTime) {

        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }

}
