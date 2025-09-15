package com.parksupark.soomjae.server.community.post.meetingpost.service;

import static com.parksupark.soomjae.server.common.exception.ErrorMessages.ALREADY_PARTICIPATE_IN_POST;
import static com.parksupark.soomjae.server.common.exception.ErrorMessages.CLOSED_MEETING;
import static com.parksupark.soomjae.server.common.exception.ErrorMessages.MEETING_PARTICIPANTS_FULL_EXCEPTION_MESSAGE;
import static com.parksupark.soomjae.server.common.exception.ErrorMessages.MEETING_POST_NOT_FOUND;
import static com.parksupark.soomjae.server.common.exception.ErrorMessages.NOT_PARTICIPANT_OF_POST;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.common.constant.PostConstant.MEETING_POST_TYPE;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.like.repository.LikeRepository;
import com.parksupark.soomjae.server.community.location.constant.LocationConstant;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.location.repository.LocationRepository;
import com.parksupark.soomjae.server.community.participation.dto.ParticipantListResponse;
import com.parksupark.soomjae.server.community.participation.dto.ParticipationResponse;
import com.parksupark.soomjae.server.community.participation.entity.Participation;
import com.parksupark.soomjae.server.community.participation.repository.ParticipationRepository;
import com.parksupark.soomjae.server.community.post.common.dto.PostListResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostDetailResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostRequest;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostStatsResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import com.parksupark.soomjae.server.community.post.meetingpost.repository.MeetingPostRepository;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingPostService {

    private final MeetingPostRepository meetingPostRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    public Long create(
        MeetingPostRequest postRequest, UsernamePasswordUserDetails userDetails) {

        Category category = getCategory(postRequest);
        Location location = getLocation(postRequest);
        Member member = userDetails.getMember();

        MeetingPost entity = postRequest.toEntity(member, category, location);

        return meetingPostRepository.save(entity).getId();
    }

    public PostListResponse readMeetingPostList(Pageable pageable,
        UsernamePasswordUserDetails userDetails) {
        List<MeetingPost> posts = meetingPostRepository.findAll(pageable).getContent();
        List<MeetingPostResponse> response = getMeetingPostStats(posts,
            userDetails.getMember().getId());
        return PostListResponse.of(response);
    }

    public PostListResponse readByMemberId(Long memberId, Pageable pageable) {
        List<MeetingPost> posts = meetingPostRepository.findByMemberId(memberId, pageable)
            .getContent();
        List<MeetingPostResponse> response = getMeetingPostStats(posts, memberId);
        return PostListResponse.of(response);
    }

    private List<MeetingPostResponse> getMeetingPostStats(List<MeetingPost> contents,
        Long memberId) {
        List<MeetingPostStatsResponse> postStats = meetingPostRepository.findPostStats(
            contents.stream().map(MeetingPost::getId).toList(), memberId);

        List<MeetingPostResponse> response = new ArrayList<>();

        for (MeetingPostStatsResponse postStat : postStats) {
            Optional<MeetingPost> postOptional = contents.stream()
                .filter(p -> postStat.getPostId() == p.getId()).findFirst();

            postOptional.ifPresent(
                meetingPost -> response.add(MeetingPostResponse.of(meetingPost, postStat)));
        }
        return response;
    }

    public MeetingPostDetailResponse readByPostId(Long postId,
        UsernamePasswordUserDetails userDetails) {
        MeetingPost meetingPost = meetingPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(MEETING_POST_NOT_FOUND));

        List<CommentResponse> comments = commentRepository
            .findByPostTypeAndPostIdAndDeletedTimeIsNull(
                MEETING_POST_TYPE, meetingPost.getId()).stream().map(CommentResponse::of)
            .toList();

        Boolean isLikedByMe = likeRepository.existsByPostTypeAndPostIdAndMemberId(
            MEETING_POST_TYPE, meetingPost.getId(), userDetails.getMember().getId());

        Long likeNum = likeRepository.countByPostTypeAndPostId(MEETING_POST_TYPE,
            meetingPost.getId());

        long currentParticipantCount = participationRepository.countByMeetingPostId(postId);

        boolean isParticipatedByMe = participationRepository.existsByMeetingPostIdAndParticipantId(
            postId, userDetails.getMember().getId());

        return MeetingPostDetailResponse.of(meetingPost, likeNum, isLikedByMe, comments,
            (int) currentParticipantCount, isParticipatedByMe);
    }


    @Transactional
    public Long update(Long meetingPostId, MeetingPostRequest meetingPostRequest) {
        MeetingPost meetingPost = meetingPostRepository.findById(meetingPostId)
            .orElseThrow(() -> new IllegalStateException(MEETING_POST_NOT_FOUND));
        updateCommunityPost(meetingPostRequest, meetingPost);
        return meetingPost.getId();
    }

    private void updateCommunityPost(MeetingPostRequest meetingPostRequest,
        MeetingPost meetingPost) {

        Category category = getCategory(meetingPostRequest);
        Location location = getLocation(meetingPostRequest);
        meetingPost.setTitle(meetingPostRequest.getTitle());
        meetingPost.setContent(meetingPostRequest.getContent());
        meetingPost.setCategory(category);
        meetingPost.setLocation(location);
    }

    @Transactional
    public void delete(Long postId) {
        MeetingPost meetingPost = meetingPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(MEETING_POST_NOT_FOUND));
        meetingPostRepository.delete(meetingPost);
    }

    private Category getCategory(MeetingPostRequest meetingPostRequest) {
        return meetingPostRequest.getCategory() != null ? categoryRepository.findById(
                Long.parseLong(meetingPostRequest.getCategory()))
            .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND)) : null;
    }

    private Location getLocation(MeetingPostRequest meetingPostRequest) {
        return meetingPostRequest.getLocation() != null ? locationRepository.findByCode(
                Long.parseLong(meetingPostRequest.getLocation()))
            .orElseThrow(() -> new IllegalStateException(LocationConstant.LOCATION_NOT_FOUND))
            : null;
    }


    @Transactional
    public ParticipationResponse participate(Long postId, UsernamePasswordUserDetails userDetails) {
        // 락을 걸고 모집글 조회
        MeetingPost meetingPost = meetingPostRepository.findByIdForUpdate(postId)
            .orElseThrow(() -> new IllegalStateException(MEETING_POST_NOT_FOUND));

        Member participant = userDetails.getMember();

        long participantsNum = participationRepository.countByMeetingPostId(postId);

        if (participantsNum >= meetingPost.getMaximumParticipants()) {
            throw new IllegalStateException(MEETING_PARTICIPANTS_FULL_EXCEPTION_MESSAGE);
        }
        if (participationRepository.existsByMeetingPostIdAndParticipantId(postId,
            participant.getId())) {
            throw new IllegalStateException(ALREADY_PARTICIPATE_IN_POST);
        }

        participationRepository.save(new Participation(participant, meetingPost));

        return ParticipationResponse.of(meetingPost.getId(), participantsNum + 1,
            meetingPost.getMaximumParticipants());
    }

    @Transactional
    public String cancelParticipation(Long postId, UsernamePasswordUserDetails userDetails) {
        Member participant = userDetails.getMember();

        MeetingPost meetingPost = meetingPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(
                MEETING_POST_NOT_FOUND));

        if (meetingPost.getEndTime().isBefore(Instant.now())) {
            throw new IllegalStateException(CLOSED_MEETING);
        }

        Participation participation = participationRepository.findByMeetingPostIdAndParticipantId(
                postId, participant.getId())
            .orElseThrow(() -> new IllegalStateException(NOT_PARTICIPANT_OF_POST));

        participationRepository.delete(participation);
        return "참여 취소 성공";
    }

    public ParticipantListResponse findAllParticipantsByPostId(Long postId) {
        return ParticipantListResponse.of(
            participationRepository.findByMeetingPostId(postId).stream()
                .map(participation -> MemberResponse.of(participation.getParticipant())).toList());
    }
}
