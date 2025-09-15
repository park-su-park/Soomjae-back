package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import lombok.Getter;

@Getter
public class MeetingPostStatsResponse {

    private Long postId;
    private Long commentCount;
    private Long likeCount;
    private Long participantCount;
    private boolean likedByMe;

    public MeetingPostStatsResponse(Long postId, Long commentCount, Long likeCount,
        Long participantCount,
        boolean likedByMe) {
        this.postId = postId;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.participantCount = participantCount;
        this.likedByMe = likedByMe;
    }
}
