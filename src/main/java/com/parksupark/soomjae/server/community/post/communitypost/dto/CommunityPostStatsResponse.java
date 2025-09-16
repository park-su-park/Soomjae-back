package com.parksupark.soomjae.server.community.post.communitypost.dto;

import lombok.Getter;

@Getter
public class CommunityPostStatsResponse {

    private Long postId;
    private Long commentCount;
    private Long likeCount;
    private boolean likedByMe;

    public CommunityPostStatsResponse(Long postId, Long commentCount, Long likeCount,
        boolean likedByMe) {
        this.postId = postId;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
    }
}
