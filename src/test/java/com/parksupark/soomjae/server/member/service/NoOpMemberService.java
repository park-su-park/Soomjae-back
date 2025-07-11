package com.parksupark.soomjae.server.member.service;

import com.parksupark.soomjae.server.member.dto.CheckDuplicateEmailResponse;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.MemberResponse;

public class NoOpMemberService implements MemberService {

    @Override
    public MemberResponse updateNickname(Long id, String nickname) {
        return null;
    }

    @Override
    public MemberResponse updatePassword(Long id, String password) {
        return null;
    }

    @Override
    public MemberResponse updateEmail(Long id, String email) {
        return null;
    }

    @Override
    public MemberResponse readMember(Long id) {
        return null;
    }

    @Override
    public MemberResponse createMember(CreateMemberRequest request) {
        return null;
    }

    @Override
    public CheckDuplicateEmailResponse isDuplicateEmail(String email) {
        return null;
    }
}
