package com.parksupark.soomjae.server.community.like.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.like.dto.LikeStatusResponse;
import com.parksupark.soomjae.server.community.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/v1/boards")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardType}/posts/{postId}/like")
    public ResponseEntity<LikeStatusResponse> postLike(
        @PathVariable("boardType") String postType,
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        LikeStatusResponse response = likeService.createLike(postType, postId, userDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{boardType}/posts/{postId}/like")
    public ResponseEntity<LikeStatusResponse> deleteLike(
        @PathVariable("boardType") String postType,
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        LikeStatusResponse response = likeService.deleteLike(postType, postId,
            userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{boardType}/posts/{postId}/like")
    public ResponseEntity<LikeStatusResponse> getLikeStatus(
        @PathVariable("boardType") String postType,
        @PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        LikeStatusResponse response = likeService.readLikeStatus(postType, postId, userDetails);
        return ResponseEntity.ok(response);
    }
}
