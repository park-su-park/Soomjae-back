package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;

public interface ProfileService {

    Long createProfile(Member member);

    ProfileResponse readProfileById(Long profileId);

    ProfileResponse readProfileByMemberId(Long memberId);

    ProfileResponse updateProfile(UpdateProfileRequest request, UsernamePasswordUserDetails userDetails);


}
