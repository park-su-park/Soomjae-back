package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceAlreadyExistsException;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;
import com.parksupark.soomjae.server.profile.entity.Profile;
import com.parksupark.soomjae.server.profile.entity.ProfileImage;
import com.parksupark.soomjae.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultProfileService implements ProfileService {

    private final ProfileRepository profileRepository;

    @Value("${app.default.profile-image-url}")
    private String defaultProfileImageUrl;

    @Override
    @Transactional
    public Long createProfile(Member member) {
        if (profileRepository.existsByMemberId(member.getId())) {
            throw new ResourceAlreadyExistsException(ErrorMessages.PROFILE_ALREADY_EXISTS_MESSAGE);
        }

        Profile profile = Profile.create(member);
        ProfileImage profileImage = ProfileImage.create(defaultProfileImageUrl);

        profile.setProfileImage(profileImage);
        Profile savedProfile = profileRepository.save(profile);
        return savedProfile.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse readProfileByMemberId(Long memberId) {
        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        return new ProfileResponse(profile.getMember().getId(), profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse readProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        return new ProfileResponse(profile.getMember().getId(), profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl());
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request,
            UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();
        Long memberId = member.getId();

        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        profile.updateProfile(request);
        return new ProfileResponse(memberId, profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl());
    }
}
