package com.parksupark.soomjae.server.member.dto;

import com.parksupark.soomjae.server.member.Role;
import com.parksupark.soomjae.server.member.entity.Member;
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
    private final String profileImageUrl;

    public MemberResponse(Long memberId, String email, String nickname, Role role,
        Instant createdTime, Instant modifiedTime, String profileImageUrl) {

        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.profileImageUrl = profileImageUrl;
    }

    public static MemberResponse create(MemberBasicInfo memberBasicInfo, String profileImageUrl) {
        return new MemberResponse(
            memberBasicInfo.getId(),
            memberBasicInfo.getEmail(),
            memberBasicInfo.getNickname(),
            memberBasicInfo.getRole(),
            memberBasicInfo.getCreatedTime(),
            memberBasicInfo.getModifiedTime(),
            profileImageUrl
        );
    }

    public static MemberResponse create(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getNickname(),
            member.getRole(), member.getCreatedTime(), member.getModifiedTime(),
            member.getProfile().getProfileImage().getImageUrl());
    }

}
