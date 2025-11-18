package com.parksupark.soomjae.server.community.like.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.community.common.exception.AlreadyLikedException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.common.exception.LikeNotFoundException;
import com.parksupark.soomjae.server.community.like.dto.LikeStatusResponse;
import com.parksupark.soomjae.server.community.like.entity.Like;
import com.parksupark.soomjae.server.community.like.repository.LikeRepository;
import com.parksupark.soomjae.server.community.validator.PostValidator;
import com.parksupark.soomjae.server.community.validator.PostValidatorFactory;
import com.parksupark.soomjae.server.fcm.service.AlarmNotificationService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultLikeService implements LikeService {

    private final LikeRepository likeRepository;
    private final PostValidatorFactory postValidatorFactory;
    private final AlarmNotificationService alarmNotificationService;

    @Override
    @Transactional
    public LikeStatusResponse createLike(String postType, Long postId,
        UsernamePasswordUserDetails userDetails) {

        validatePost(postType, postId);

        // 이미 좋아요 눌렀는지 확인
        if (likeRepository.existsByPostTypeAndPostIdAndMemberId(postType, postId,
            userDetails.getMember().getId())) {
            throw new AlreadyLikedException(ErrorMessages.ALREADY_LIKED_EXCEPTION_MESSAGE);
        }

        Like like = Like.builder()
            .postType(postType)
            .postId(postId)
            .member(userDetails.getMember())
            .build();

        likeRepository.save(like);
        alarmNotificationService.sendNewLikeAlarm(like);
        Long likeCount = likeRepository.countByPostTypeAndPostId(postType, postId);

        // liked=true 를 하드코딩해서 결과로 보내는 중인데
        // 실제로 DB에 잘 반영 되었는지 검증을 먼저 해야하는지 궁금합니다.
        return new LikeStatusResponse(true, likeCount);
    }

    @Override
    @Transactional
    public LikeStatusResponse deleteLike(String postType, Long postId,
        UsernamePasswordUserDetails userDetails) {

        validatePost(postType, postId);

        Like like = likeRepository.findByPostTypeAndPostIdAndMemberId(postType, postId,
                userDetails.getMember().getId())
            .orElseThrow(
                () -> new LikeNotFoundException(
                    ErrorMessages.LIKE_NOT_FOUND_EXCEPTION_MESSAGE));

        likeRepository.delete(like);
        Long likeCount = likeRepository.countByPostTypeAndPostId(postType, postId);

        return new LikeStatusResponse(false, likeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public LikeStatusResponse readLikeStatus(String postType, Long postId,
        UsernamePasswordUserDetails userDetails) {
            @Nullable UsernamePasswordUserDetails userDetails) {

        validatePost(postType, postId);

        Long likeCount = likeRepository.countByPostTypeAndPostId(postType, postId);

        boolean liked =
            userDetails != null && likeRepository.existsByPostTypeAndPostIdAndMemberId(postType,
                postId,
                userDetails.getMember().getId());

        return new LikeStatusResponse(liked, likeCount);
    }

    private void validatePost(String postType, Long postId) {
        PostValidator validator = postValidatorFactory.getValidator(postType);
        if (!validator.isValid(postId)) {
            throw new InvalidPostIdException(
                ErrorMessages.INVALID_POST_ID_EXCEPTION_MESSAGE + postId);
        }
    }
}
