package com.parksupark.soomjae.server.profile.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceAlreadyExistsException;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameRequest;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameResponse;
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
    private final MemberRepository memberRepository;

    @Value("${app.profile.default-image-url}")
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
            profile.getProfileImage().getImageUrl(), profile.getMember().getNickname());
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request,
            UsernamePasswordUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();

        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorMessages.MEMBER_NOT_FOUND_EXCEPTION_MESSAGE));

        String newNickname = request.getNickname();
        String newBio = request.getBio();
        String newProfileImageUrl = request.getProfileImageUrl();

        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        if (newNickname != null) {
            if (memberRepository.existsByNickname(newNickname)) {
                throw new ResourceAlreadyExistsException(
                    ErrorMessages.NICKNAME_ALREADY_USED_MESSAGE);
            }
            member.updateNickname(request.getNickname());
        }

        if (newBio != null) {
            profile.setBio(newBio);
        }

        if (newProfileImageUrl != null) {
            profile.setProfileImageUrl(newProfileImageUrl);
        }

        return new ProfileResponse(memberId, profile.getId(), profile.getBio(),
            profile.getProfileImage().getImageUrl(), profile.getMember().getNickname());
    }

    @Override
    public CheckDuplicateNicknameResponse checkDuplicateNickname(
        CheckDuplicateNicknameRequest request) {

        boolean duplicate = memberRepository.existsByNickname(request.getNickname());

        return new CheckDuplicateNicknameResponse(duplicate);
    }
}
