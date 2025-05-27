package com.parksupark.soomjae.server.member.dto;

import com.parksupark.soomjae.server.member.entity.Member;

public class MemberResponse {
    private Long memberId;

    private String email;

    public MemberResponse(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }

}
