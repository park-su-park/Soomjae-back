package com.parksupark.soomjae.server.community.review.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.review.dto.CreateReviewRequest;
import com.parksupark.soomjae.server.community.review.dto.ReviewExistenceResponse;
import com.parksupark.soomjae.server.community.review.dto.ReviewResponse;
import com.parksupark.soomjae.server.community.review.dto.UpdateReviewRequest;
import com.parksupark.soomjae.server.community.review.service.ReviewService;
import jakarta.validation.Valid;
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
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/boards/meeting/posts/{postId}/reviews")
    public ResponseEntity<ReviewResponse> postReview(@PathVariable Long postId,
        @RequestBody @Valid CreateReviewRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        ReviewResponse response = reviewService.createReview(postId, request,
            userDetails.getMember());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/boards/meeting/posts/{postId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long postId,
        @PathVariable Long reviewId) {

        ReviewResponse response = reviewService.readReview(reviewId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/boards/meeting/posts/{postId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> putReview(@PathVariable Long postId,
        @PathVariable Long reviewId,
        @RequestBody @Valid UpdateReviewRequest request,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        ReviewResponse response = reviewService.updateReview(reviewId, request,
            userDetails.getMember());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/boards/meeting/posts/{postId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long postId,
        @PathVariable Long reviewId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getMember());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/boards/meeting/posts/{postId}/reviews/me")
    public ResponseEntity<ReviewExistenceResponse> checkReviewExistence(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {

        ReviewExistenceResponse response = reviewService.checkReviewExistence(postId,
            userDetails.getMember());

        return ResponseEntity.ok(response);
    }
}
