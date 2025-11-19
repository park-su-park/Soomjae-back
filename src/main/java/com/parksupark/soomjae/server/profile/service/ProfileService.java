package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameRequest;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameResponse;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;

public interface ProfileService {

    Long createProfile(Member member);

    ProfileResponse readProfileByMemberId(Long memberId);

    ProfileResponse updateProfile(UpdateProfileRequest request,
        UsernamePasswordUserDetails userDetails);

    CheckDuplicateNicknameResponse checkDuplicateNickname(CheckDuplicateNicknameRequest request);

}
