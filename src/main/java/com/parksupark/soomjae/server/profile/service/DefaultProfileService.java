package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceAlreadyExistsException;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.profile.dto.CreateProfileRequest;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;
import com.parksupark.soomjae.server.profile.entity.Profile;
import com.parksupark.soomjae.server.profile.entity.ProfileImage;
import com.parksupark.soomjae.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultProfileService implements ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    @Override
    public ProfileResponse createProfile(CreateProfileRequest request,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();
        Long memberId = member.getId();

        if (profileRepository.existsByMemberId(memberId)) {
            throw new ResourceAlreadyExistsException(ErrorMessages.PROFILE_ALREADY_EXISTS_MESSAGE);
        }

        Profile profile = Profile.create(request.getBio(), member, request.getNickname());
        ProfileImage profileImage = ProfileImage.create(request.getProfileImageUrl());
        profile.setProfileImage(profileImage);

        profileRepository.save(profile);

        return new ProfileResponse(memberId, profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl(), profile.getNickname());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse readProfileByMemberId(Long memberId) {
        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        return new ProfileResponse(profile.getMember().getId(), profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl(), profile.getNickname());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse readProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        return new ProfileResponse(profile.getMember().getId(), profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl(), profile.getNickname());
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
            profile.getProfileImage().getImageUrl(), profile.getNickname());
    }
}
