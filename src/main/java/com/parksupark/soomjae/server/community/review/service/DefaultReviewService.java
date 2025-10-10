package com.parksupark.soomjae.server.community.review.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.community.participation.entity.Participation;
import com.parksupark.soomjae.server.community.participation.repository.ParticipationRepository;
import com.parksupark.soomjae.server.community.post.meetingpost.repository.MeetingPostRepository;
import com.parksupark.soomjae.server.community.review.dto.CreateReviewRequest;
import com.parksupark.soomjae.server.community.review.dto.ReviewExistenceResponse;
import com.parksupark.soomjae.server.community.review.dto.ReviewResponse;
import com.parksupark.soomjae.server.community.review.dto.UpdateReviewRequest;
import com.parksupark.soomjae.server.community.review.entity.Review;
import com.parksupark.soomjae.server.community.review.repository.ReviewRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ParticipationRepository participationRepository;
    private final ReviewRepository reviewRepository;
    private final MeetingPostRepository meetingPostRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(Long postId, CreateReviewRequest request, Member member) {

        Participation participation = participationRepository.findByMeetingPostIdAndParticipantId(
                postId, member.getId())
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.NOT_PARTICIPANT_OF_POST));

        if (reviewRepository.existsByParticipationId(participation.getId())) {
            throw new IllegalStateException(ErrorMessages.REVIEW_ALREADY_EXISTS_MESSAGE);
        }

        Review review = Review.create(participation, request.getStar(), request.getContent());
        reviewRepository.save(review);

        return new ReviewResponse(review.getId(), participation.getMeetingPost().getId(),
            review.getStar(),
            review.getContent(), review.getCreatedTime());
    }

    // 조회 메서드는 필요에 따라 더 추가 
    // ex) 특정 MeetingPost에 달린 리뷰 다건 조회, 특정 Member가 작성한 리뷰 다건 조회
    @Override
    @Transactional(readOnly = true)
    public ReviewResponse readReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.REVIEW_NOT_FOUND_MESSAGE));

        return new ReviewResponse(review.getId(),
            review.getParticipation().getParticipant().getId(), review.getStar(),
            review.getContent(), review.getCreatedTime());
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, Member member) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.REVIEW_NOT_FOUND_MESSAGE));

        if (!review.getParticipation().getParticipant().getId().equals(member.getId())) {
            throw new IllegalStateException(ErrorMessages.REVIEW_OWNER_MISMATCH_MESSAGE);
        }

        review.updateReview(request);
        return new ReviewResponse(review.getId(), review.getParticipation().getId(),
            review.getStar(), review.getContent(), review.getCreatedTime());
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.REVIEW_NOT_FOUND_MESSAGE));

        if (!review.getParticipation().getParticipant().getId().equals(member.getId())) {
            throw new IllegalStateException(ErrorMessages.REVIEW_OWNER_MISMATCH_MESSAGE);
        }

        reviewRepository.delete(review);
    }

    @Override
    public ReviewExistenceResponse checkReviewExistence(Long postId, Member member) {
        Participation participation = participationRepository.findByMeetingPostIdAndParticipantId(
                postId, member.getId())
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.NOT_PARTICIPANT_OF_POST));

        Optional<Long> reviewIdOptional = reviewRepository.findReviewIdByParticipationId(
            participation.getId());

        return reviewIdOptional
            .map(reviewId -> new ReviewExistenceResponse(true, reviewId))
            .orElse(new ReviewExistenceResponse(false, null));
    }
}
