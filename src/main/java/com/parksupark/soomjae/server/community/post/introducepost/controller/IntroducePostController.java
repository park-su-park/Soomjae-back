package com.parksupark.soomjae.server.community.post.introducepost.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introducepost.dto.CreateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.dto.UpdateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.service.IntroducePostService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/members/introduce-post")
@RequiredArgsConstructor
public class IntroducePostController {

    private final IntroducePostService introducePostService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> postIntroducePost(@RequestBody CreateIntroducePostRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        Long postId = introducePostService.createIntroducePost(request, userDetails);

        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getIntroducePostByPostId(@PathVariable Long postId) {

        PostResponse response = introducePostService.readIntroducePostById(postId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-memberId/{memberId}")
    public ResponseEntity<PostResponse> getIntroducePostByMemberId(@PathVariable Long memberId) {

        PostResponse response = introducePostService.readIntroducePostByMemberId(memberId);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponse> putIntroducePost(
        @RequestBody UpdateIntroducePostRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        PostResponse response = introducePostService.updateIntroducePost(request, userDetails);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteIntroducePostByPostId(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        introducePostService.deleteIntroducePostById(postId, userDetails);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/by-memberId/{memberId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteIntroducePostByMemberId(@PathVariable Long memberId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        introducePostService.deleteIntroducePostByMemberId(memberId, userDetails);

        return ResponseEntity.ok().build();
    }
}
