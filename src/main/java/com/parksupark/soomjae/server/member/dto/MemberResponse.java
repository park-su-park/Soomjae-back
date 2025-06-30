package com.parksupark.soomjae.server.member.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Instant createdTime;
    private final Instant modifiedTime;
}
