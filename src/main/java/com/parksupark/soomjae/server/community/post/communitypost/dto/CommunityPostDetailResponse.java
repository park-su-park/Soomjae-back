package com.parksupark.soomjae.server.community.post.communitypost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import java.util.List;
import lombok.Getter;

@Getter
public class CommunityPostDetailResponse extends CommunityPostBaseResponse {

    private final List<CommentResponse> comments;

    private CommunityPostDetailResponse(CommunityPost communityPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        super(communityPost, likeNum, isLikedByMe);
        this.comments = comments;
    }

    public static CommunityPostDetailResponse of(CommunityPost communityPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        return new CommunityPostDetailResponse(communityPost, likeNum, isLikedByMe, comments);
    }
}