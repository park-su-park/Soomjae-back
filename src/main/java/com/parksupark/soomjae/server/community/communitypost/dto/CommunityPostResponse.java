package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.category.dto.PostCategoryResponse;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import lombok.Getter;


@Getter
public class CommunityPostResponse {

    private Long postId;
    private String postType;

    private String title;

    private String content;

    private MemberResponse author;

    private PostCategoryResponse category;

    public CommunityPostResponse(Long postId, String title, String content,
            MemberResponse author, PostCategoryResponse postCategoryResponse) {
        this.postId = postId;
        this.postType = "community";
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = postCategoryResponse;
    }

    public static CommunityPostResponse of(CommunityPost communityPost) {
        return new CommunityPostResponse(communityPost.getId(), communityPost.getTitle(),
                communityPost.getContent(), MemberResponse.of(communityPost.getMember()),
                PostCategoryResponse.of(communityPost.getCategory()));
    }
}
