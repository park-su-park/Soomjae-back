package com.parksupark.soomjae.server.fcm.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.community.comment.entity.Comment;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.like.entity.Like;
import com.parksupark.soomjae.server.community.participation.entity.Participation;
import com.parksupark.soomjae.server.community.post.communitypost.repository.CommunityPostRepository;
import com.parksupark.soomjae.server.community.post.meetingpost.repository.MeetingPostRepository;
import com.parksupark.soomjae.server.community.validator.PostValidator;
import com.parksupark.soomjae.server.community.validator.PostValidatorFactory;
import com.parksupark.soomjae.server.fcm.domain.Token;
import com.parksupark.soomjae.server.fcm.dto.AlarmDto;
import com.parksupark.soomjae.server.fcm.repository.TokenRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmNotificationService {

    private final PostValidatorFactory postValidatorFactory;
    private final CommentRepository commentRepository;
    private final TokenRepository tokenRepository;
    private final CommunityPostRepository communityPostRepository;
    private final MeetingPostRepository meetingPostRepository;
    private final FCMService fcmService;

    @Transactional
    public void sendNewCommentAlarm(Comment comment) {

        Member writer = comment.getMember();

        List<Member> receivers = new ArrayList<>();

        // 댓글 단 사람들 추가
        List<Member> commenters = commentRepository.findMembersByPost(comment.getPostType(),
            comment.getPostId());

        receivers.addAll(commenters);

        // 2) 중복 제거 + 이번에 댓글 쓴 사람 제외
        receivers = receivers.stream()
            .distinct()
            .filter(m -> !m.getId().equals(writer.getId()))
            .toList();

        if (receivers.isEmpty()) {
            return;
        }

        // 3) 멤버들의 유효 토큰 조회
        LocalDate today = LocalDate.now();

        List<Token> tokens = tokenRepository.findByMemberInAndExpirationDateAfter(receivers, today);
        if (tokens.isEmpty()) {
            return;
        }

        validatePost(comment.getPostType(), comment.getPostId());
        String postTitle = getPostTitle(comment.getPostType(), comment.getPostId());

        // 4) AlarmDto 생성 (타이틀/내용/이미지/URL은 서비스 정책에 맞게)
        AlarmDto alarmDto = AlarmDto.builder()
            .title("새 댓글이 달렸어요")
            .content(postTitle + " 글에 새로운 댓글이 달렸어요.")
            .url("/v1/boards/" + comment.getPostType() + "/posts/"
                + comment.getPostId()) // 프론트 라우팅에 맞게 수정
            .build();

        // 5) FCM 발사
        fcmService.sendByAlarm(alarmDto, tokens);
    }

    @Transactional
    public void sendNewLikeAlarm(Like like) {
        LocalDate today = LocalDate.now();

        validatePost(like.getPostType(), like.getPostId());
        Member postOwner = getPostOwner(like);

        List<Token> tokens = tokenRepository.findByMemberInAndExpirationDateAfter(
            List.of(postOwner), today);
        if (tokens.isEmpty()) {
            return;
        }

        String postTitle = getPostTitle(like.getPostType(), like.getPostId());

        // 4) AlarmDto 생성 (타이틀/내용/이미지/URL은 서비스 정책에 맞게)
        AlarmDto alarmDto = AlarmDto.builder()
            .title("새로운 좋아요가 생겼어요")
            .content("내가 작성한 " + postTitle + " 글을 " + like.getMember().getNickname() + "님이 좋아합니다.")
            .url("/v1/boards/" + like.getPostType() + "/posts/"
                + like.getPostId()) // 프론트 라우팅에 맞게 수정
            .build();

        // 5) FCM 발사
        fcmService.sendByAlarm(alarmDto, tokens);
    }

    private void validatePost(String postType, Long postId) {
        PostValidator validator = postValidatorFactory.getValidator(postType);
        if (!validator.isValid(postId)) {
            throw new InvalidPostIdException(
                ErrorMessages.INVALID_POST_ID_EXCEPTION_MESSAGE + postId);
        }
    }

    private String getPostTitle(String postType, Long postId) {
        switch (postType) {
            case PostConstant.COMMUNITY_POST_TYPE:
                return communityPostRepository.findById(postId)
                    .orElseThrow(
                        () -> new IllegalArgumentException(ErrorMessages.COMMUNITY_POST_NOT_FOUND))
                    .getTitle();

            case PostConstant.MEETING_POST_TYPE:
                return meetingPostRepository.findById(postId)
                    .orElseThrow(
                        () -> new IllegalArgumentException(ErrorMessages.MEETING_POST_NOT_FOUND))
                    .getTitle();

            default:
                throw new IllegalArgumentException("Unknown postType: " + postType);
        }
    }

    private Member getPostOwner(Like like) {
        String postType = like.getPostType();
        switch (postType) {
            case PostConstant.COMMUNITY_POST_TYPE:
                return communityPostRepository.findById(like.getPostId())
                    .orElseThrow(
                        () -> new IllegalArgumentException(ErrorMessages.COMMUNITY_POST_NOT_FOUND))
                    .getMember();

            case PostConstant.MEETING_POST_TYPE:
                return meetingPostRepository.findById(like.getPostId())
                    .orElseThrow(
                        () -> new IllegalArgumentException(ErrorMessages.MEETING_POST_NOT_FOUND))
                    .getMember();

            default:
                throw new IllegalArgumentException("Unknown postType: " + postType);
        }
    }

    public void sendParticipationAlarm(Participation participation) {

        LocalDate today = LocalDate.now();

        Member postOwner = participation.getMeetingPost().getMember();

        List<Token> tokens = tokenRepository.findByMemberInAndExpirationDateAfter(
            List.of(postOwner), today);
        if (tokens.isEmpty()) {
            return;
        }

        String postTitle = participation.getMeetingPost().getTitle();

        // 4) AlarmDto 생성 (타이틀/내용/이미지/URL은 서비스 정책에 맞게)
        AlarmDto alarmDto = AlarmDto.builder()
            .title("새로운 참여자가 생겼어요")
            .content("내가 작성한 " + postTitle + " 모임에 " + participation.getParticipant().getNickname()
                + "님이 참여합니다.")
            .url("/v1/boards/" + PostConstant.MEETING_POST_TYPE + "/posts/"
                + participation.getMeetingPost().getId()) // 프론트 라우팅에 맞게 수정
            .build();

        // 5) FCM 발사
        fcmService.sendByAlarm(alarmDto, tokens);
    }
}
