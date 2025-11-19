package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingPostResponseWithComments {

    private final Long postId;
    private final String postType;
    private final String title;
    private final String content;

    private final MemberResponse author;

    @Nullable
    private final String category;
    @Nullable
    private final String location;

    private final Instant createdTime;

    private final Long likeNum;
    private final Boolean isLikedByMe;

    private final int maximumParticipants;
    private final int currentParticipantCount;

    private final Instant startTime;
    private final Instant endTime;

    private final List<CommentResponse> comments;

    private final Boolean isParticipatedByMe;


    private MeetingPostResponseWithComments(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments,
        int currentParticipantCount, Boolean isParticipatedByMe) {
        this.postId = meetingPost.getId();
        this.postType = PostConstant.MEETING_POST_TYPE;
        this.title = meetingPost.getTitle();
        this.content = meetingPost.getContent();
        this.author = MemberResponse.create(meetingPost.getMember());
        this.category = meetingPost.getCategory().getName();
        this.location = meetingPost.getLocation().getName();
        this.createdTime = meetingPost.getCreatedTime();
        this.likeNum = likeNum;
        this.isLikedByMe = isLikedByMe;
        this.maximumParticipants = meetingPost.getMaximumParticipants();
        this.currentParticipantCount = currentParticipantCount;
        this.startTime = meetingPost.getStartTime();
        this.endTime = meetingPost.getEndTime();
        this.comments = comments;
        this.isParticipatedByMe = isParticipatedByMe;
    }

    public static MeetingPostResponseWithComments of(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments, int currentParticipantCount,
        Boolean isParticipatedByMe) {
        return new MeetingPostResponseWithComments(meetingPost, likeNum, isLikedByMe, comments,
            currentParticipantCount, isParticipatedByMe);
    }
}