package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import lombok.Getter;

@Getter
public class CommunityPostResponse extends CommunityPostBaseResponse {

    private final Long commentNum;

    private CommunityPostResponse(CommunityPost communityPost, Long commentNum) {
        super(communityPost);
        this.commentNum = commentNum;
    }

    public static CommunityPostResponse of(CommunityPost communityPost, Long commentNum) {
        return new CommunityPostResponse(communityPost, commentNum);
    }
}
