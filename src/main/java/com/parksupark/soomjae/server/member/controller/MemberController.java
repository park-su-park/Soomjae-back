package com.parksupark.soomjae.server.member.controller;

import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.CreateMemberResponse;
import com.parksupark.soomjae.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/create-member")
    public ResponseEntity<CreateMemberResponse> postMember(
        @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = memberService.createMember(request);
        return ResponseEntity.ok(response);
    }

}
