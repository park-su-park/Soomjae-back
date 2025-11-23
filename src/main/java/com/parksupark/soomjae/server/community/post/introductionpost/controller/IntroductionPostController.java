package com.parksupark.soomjae.server.community.post.introductionpost.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introductionpost.dto.UpdateIntroductionPostRequest;
import com.parksupark.soomjae.server.community.post.introductionpost.service.IntroductionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members/introduce-post")
@RequiredArgsConstructor
public class IntroductionPostController {

    private final IntroductionPostService introductionPostService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getIntroducePostByPostId(@PathVariable Long postId) {

        PostResponse response = introductionPostService.readIntroductionPostById(postId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-memberId/{memberId}")
    public ResponseEntity<PostResponse> getIntroducePostByMemberId(@PathVariable Long memberId) {

        PostResponse response = introductionPostService.readIntroductionPostByMemberId(memberId);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponse> putIntroducePost(
        @RequestBody UpdateIntroductionPostRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        PostResponse response = introductionPostService.updateIntroductionPost(request, userDetails);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteIntroducePostByPostId(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        introductionPostService.deleteIntroductionPostById(postId, userDetails);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/by-memberId/{memberId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteIntroducePostByMemberId(@PathVariable Long memberId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        introductionPostService.deleteIntroductionPostByMemberId(memberId, userDetails);

        return ResponseEntity.ok().build();
    }
}
