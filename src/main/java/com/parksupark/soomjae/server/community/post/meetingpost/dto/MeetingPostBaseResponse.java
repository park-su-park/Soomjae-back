package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import jakarta.annotation.Nullable;
import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class MeetingPostBaseResponse {

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

    protected MeetingPostBaseResponse(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, int currentParticipantCount) {
        this.postId = meetingPost.getId();
        this.postType = "meeting";
        this.title = meetingPost.getTitle();
        this.content = meetingPost.getContent();
        this.author = MemberResponse.create(meetingPost.getMember());
        this.createdTime = meetingPost.getCreatedTime();
        this.category =
            meetingPost.getCategory() != null ? meetingPost.getCategory().getName() : null;
        this.location =
            meetingPost.getLocation() != null ? meetingPost.getLocation().getName() : null;
        this.likeNum = likeNum;
        this.isLikedByMe = isLikedByMe;
        this.maximumParticipants = meetingPost.getMaximumParticipants();
        this.startTime = meetingPost.getStartTime();
        this.endTime = meetingPost.getEndTime();
        this.currentParticipantCount = currentParticipantCount;
    }
}
