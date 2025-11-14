package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.profile.dto.CreateProfileRequest;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;

public interface ProfileService {

    ProfileResponse createProfile(CreateProfileRequest request,
        UsernamePasswordUserDetails userDetails);

    ProfileResponse readProfileById(Long profileId);

    ProfileResponse readProfileByMemberId(Long memberId);


}
