package com.parksupark.soomjae.server.community.post.memberpost.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostDetailResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostFeedResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostGridProjection;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostIdResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.SaveMemberPostRequest;
import com.parksupark.soomjae.server.community.post.memberpost.service.MemberPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards/member/posts")
public class MemberPostController {

    private final MemberPostService memberPostService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberPostIdResponse> postMemberPost(
        @Valid @RequestBody SaveMemberPostRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        MemberPostIdResponse response = memberPostService.createMemberPost(request,
            userDetails.getMember());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<MemberPostDetailResponse> getMemberPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        MemberPostDetailResponse response = memberPostService.readMemberPost(postId,
            userDetails);

        return ResponseEntity.ok(response);
    }

    // 최신순 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<Page<MemberPostFeedResponse>> getMemberPostFeed(
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails,
        Pageable pageable
    ) {
        Page<MemberPostFeedResponse> response = memberPostService.readFeedMemberPosts(
            pageable, userDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/grid")
    public ResponseEntity<Page<MemberPostGridProjection>> getMemberPostGrid(
        @PathVariable Long memberId,
        Pageable pageable
    ) {
        Page<MemberPostGridProjection> response = memberPostService.readGridMemberPosts(
            memberId, pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberPostIdResponse> putMemberPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails,
        @RequestBody @Valid SaveMemberPostRequest request
    ) {
        MemberPostIdResponse response = memberPostService.updateMemberPost(request,
            postId, userDetails.getMember());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMemberPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        memberPostService.deleteMemberPost(postId, userDetails.getMember());

        return ResponseEntity.ok().build();
    }
}
