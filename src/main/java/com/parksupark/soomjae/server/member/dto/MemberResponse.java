package com.parksupark.soomjae.server.member.dto;

import com.parksupark.soomjae.server.member.Role;
import java.time.Instant;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Instant createdTime;
    private final Instant modifiedTime;
    private final Role role;

    public MemberResponse(Long memberId, String email, String nickname,
                          Instant createdTime, Instant modifiedTime, Role role) {
        
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.role = role;
    }
}
