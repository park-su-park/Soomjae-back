package com.parksupark.soomjae.server.fcm.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.fcm.dto.RegisterFCMTokenRequest;
import com.parksupark.soomjae.server.fcm.service.FCMTokenService;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FCMTokenController {

    private FCMTokenService fcmTokenService;

    @PostMapping("/v1/fcm-token")
    public ResponseEntity<String> registerToken(@RequestBody RegisterFCMTokenRequest request,
        @AuthenticationPrincipal
        UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();
        fcmTokenService.saveFCMToken(member, request.getFcmToken());
        return ResponseEntity.ok("기기 토큰 저장성공");
    }
}
