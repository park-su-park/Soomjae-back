package com.parksupark.soomjae.server.community.review.service;

import com.parksupark.soomjae.server.community.review.dto.CreateReviewRequest;
import com.parksupark.soomjae.server.community.review.dto.ReviewResponse;
import com.parksupark.soomjae.server.community.review.dto.UpdateReviewRequest;
import com.parksupark.soomjae.server.member.entity.Member;

public interface ReviewService {

    ReviewResponse createReview(Long postId, CreateReviewRequest request, Member member);

    ReviewResponse readReview(Long reviewId);

    ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, Member member);

    void deleteReview(Long reviewId, Member member);

}
