package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import lombok.Getter;

import java.util.List;

@Getter
public class CommunityPostDetailResponse extends CommunityPostBaseResponse {

    private final List<CommentResponse> comments;

    private CommunityPostDetailResponse(CommunityPost communityPost, List<CommentResponse> comments) {
        super(communityPost);
        this.comments = comments;
    }

    public static CommunityPostDetailResponse of(CommunityPost communityPost, List<CommentResponse> comments) {
        return new CommunityPostDetailResponse(communityPost, comments);
    }
}