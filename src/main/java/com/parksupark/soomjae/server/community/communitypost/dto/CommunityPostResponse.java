package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.LocalDateTime;
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
    private LocalDateTime createdTime;

    public CommunityPostResponse(Long postId, String title, String content,
            MemberResponse author, LocalDateTime createdTime) {
        this.postId = postId;
        this.postType = "community";
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdTime = createdTime;
    }

    public static CommunityPostResponse of(CommunityPost communityPost) {
        Member author = communityPost.getMember();

        CommunityPostResponse communityPostResponse = new CommunityPostResponse(
            communityPost.getId(), communityPost.getTitle(),
            communityPost.getContent(),
            new MemberResponse(author.getId(), author.getEmail(), author.getNickname()),
            communityPost.getCreatedTime());


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
