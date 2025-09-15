package com.parksupark.soomjae.server.community.post.communitypost.dto;

import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import lombok.Getter;

@Getter
public class CommunityPostResponse extends CommunityPostBaseResponse implements PostResponse {

    private final Long commentNum;

    private CommunityPostResponse(CommunityPost communityPost, Long likeNum, Boolean isLikedByMe,
        Long commentNum) {
        super(communityPost, likeNum, isLikedByMe);
        this.commentNum = commentNum;
    }

    public static CommunityPostResponse of(CommunityPost communityPost,
        CommunityPostStatsResponse communityPostStatsResponse) {
        return new CommunityPostResponse(communityPost, communityPostStatsResponse.getLikeCount(),
            communityPostStatsResponse.isLikedByMe(), communityPostStatsResponse.getCommentCount());
    }
}
