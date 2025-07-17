package com.parksupark.soomjae.server.member.service;

import com.parksupark.soomjae.server.member.dto.CheckDuplicateEmailResponse;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.MemberResponse;

public interface MemberService {

    MemberResponse createMember(CreateMemberRequest request);

    MemberResponse readMember(Long id);

    MemberResponse updateEmail(Long id, String email);

    MemberResponse updatePassword(Long id, String password);

    MemberResponse updateNickname(Long id, String nickname);

    CheckDuplicateEmailResponse checkDuplicateEmail(String email);

}
