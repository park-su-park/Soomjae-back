package com.parksupark.soomjae.server.community.community_post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommunityPostListResponse {

    List<CommunityPostResponse> posts;

    public CommunityPostListResponse(List<CommunityPostResponse> posts) {
        this.posts = posts;
    }
}
