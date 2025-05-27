package com.parksupark.soomjae.server.community.community_post.dto;

import com.parksupark.soomjae.server.community.community_post.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;


public class CommunityPostResponse {

    private Long postId;
    private String postType;

    private String title;

    private String content;

    private MemberResponse author;

    public CommunityPostResponse(Long postId, String title, String content,
        MemberResponse author) {
        this.postId = postId;
        this.postType = "community";
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static CommunityPostResponse of(CommunityPost communityPost) {
        return new CommunityPostResponse(communityPost.getId(), communityPost.getTitle(),
            communityPost.getContent(), MemberResponse.of(communityPost.getMember()));
    }
}
