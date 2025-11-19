package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import jakarta.annotation.Nullable;
import java.time.Instant;
import lombok.Getter;

@Getter
public class MeetingPostResponse implements PostResponse {

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

    private final Long commentNum;


    private MeetingPostResponse(MeetingPost meetingPost,
        MeetingPostStatsResponse postStatsResponse) {
        this.postId = meetingPost.getId();
        this.postType = PostConstant.MEETING_POST_TYPE;
        this.title = meetingPost.getTitle();
        this.content = meetingPost.getContent();
        this.author = MemberResponse.create(meetingPost.getMember());
        this.category = meetingPost.getCategory().getName();
        this.location = meetingPost.getLocation().getName();
        this.createdTime = meetingPost.getCreatedTime();
        this.likeNum = postStatsResponse.getLikeCount();
        this.isLikedByMe = postStatsResponse.isLikedByMe();
        this.maximumParticipants = meetingPost.getMaximumParticipants();
        this.currentParticipantCount = postStatsResponse.getParticipantCount().intValue();
        this.startTime = meetingPost.getStartTime();
        this.endTime = meetingPost.getEndTime();
        this.commentNum = postStatsResponse.getCommentCount();
    }

    public static MeetingPostResponse of(MeetingPost meetingPost,
        MeetingPostStatsResponse postStatsResponse) {
        return new MeetingPostResponse(meetingPost, postStatsResponse);
    }
}
