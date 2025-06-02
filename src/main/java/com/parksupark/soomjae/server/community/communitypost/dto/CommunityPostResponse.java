package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommunityPostResponse {

    private Long postId;
    private String postType;

    private String title;

    private String content;

    private MemberResponse author;

    private String category;
    private String location;

    public CommunityPostResponse(Long postId, String title, String content,
            MemberResponse author) {
        this.postId = postId;
        this.postType = "community";
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static CommunityPostResponse of(CommunityPost communityPost) {
        CommunityPostResponse communityPostResponse = new CommunityPostResponse(
                communityPost.getId(), communityPost.getTitle(),
                communityPost.getContent(), MemberResponse.of(communityPost.getMember()));

        Category category = communityPost.getCategory();
        if (category != null) {
            communityPostResponse.setCategory(category.getName());
        }

        Location location = communityPost.getLocation();
        if (location != null) {
            communityPostResponse.setLocation(location.getName());
        }
        
        return communityPostResponse;
    }
}
