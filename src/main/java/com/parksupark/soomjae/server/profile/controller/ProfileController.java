package com.parksupark.soomjae.server.profile.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.profile.dto.CreateProfileRequest;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> postProfile(@RequestBody CreateProfileRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        ProfileResponse response = profileService.createProfile(request, userDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfileByProfileId(@PathVariable Long profileId) {
        ProfileResponse response = profileService.readProfileById(profileId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfileByMemberId(
        @RequestParam(name = "member_id") Long memberId) {
        ProfileResponse response = profileService.readProfileByMemberId(memberId);

        return ResponseEntity.ok(response);
    }
}
