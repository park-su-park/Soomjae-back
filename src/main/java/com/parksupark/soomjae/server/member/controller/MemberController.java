package com.parksupark.soomjae.server.member.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.dto.*;
import com.parksupark.soomjae.server.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/create-member")
    public ResponseEntity<MemberResponse> postMember(
        @RequestBody @Valid CreateMemberRequest request) {

        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<MemberResponse> getMyInfo(
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        MemberResponse memberResponse = memberService.readMember(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberInfo(
        @PathVariable Long memberId
    ) {
        MemberResponse memberResponse = memberService.readMember(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me/update-email")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<MemberResponse> patchMemberEmail(
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails,
        @RequestBody @Valid PatchEmailRequest request
    ) {
        String email = request.getEmail();
        Long memberId = userDetails.getMember().getId();
        MemberResponse memberResponse = memberService.updateEmail(memberId, email);
        
        return ResponseEntity.ok(memberResponse);
    }

    // 비밀번호 변경은 현재 비밀번호 검증이 필요하므로 추후에 구현
    // nickname 중복 검증 필요?
    @PatchMapping("/me/update-nickname")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<MemberResponse> patchMemberNickname(
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails,
        @RequestBody @Valid PatchNicknameRequest request
    ) {
        Long memberId = userDetails.getMember().getId();
        String nickname = request.getNickname();
        MemberResponse memberResponse = memberService.updateNickname(memberId, nickname);

        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/check-duplicate-email")
    public ResponseEntity<CheckDuplicateEmailResponse> checkEmailAvailability(
            @RequestBody @Valid CheckDuplicateEmailRequest request
    ) {
        String email = request.getEmail();
        CheckDuplicateEmailResponse response = memberService.isDuplicateEmail(email);

        return ResponseEntity.ok(response);
    }
}
