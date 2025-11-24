package com.parksupark.soomjae.server.profile.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameRequest;
import com.parksupark.soomjae.server.profile.dto.CheckDuplicateNicknameResponse;
import com.parksupark.soomjae.server.profile.dto.ProfileResponse;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;
import com.parksupark.soomjae.server.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{memberId}/profiles")
    public ResponseEntity<ProfileResponse> getProfileByMemberId(
        @PathVariable Long memberId) {
        ProfileResponse response = profileService.readProfileByMemberId(memberId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profiles")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> putProfile(@RequestBody UpdateProfileRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        ProfileResponse response = profileService.updateProfile(request, userDetails);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profiles/check-duplicate-nickname")
    public ResponseEntity<CheckDuplicateNicknameResponse> checkNicknameAvailability(
        @RequestBody CheckDuplicateNicknameRequest request) {

        return ResponseEntity.ok(profileService.checkDuplicateNickname(request));
    }
}
