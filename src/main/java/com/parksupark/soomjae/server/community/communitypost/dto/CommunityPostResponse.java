package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.Instant;
import lombok.Getter;


@Getter
public class CommunityPostResponse {

    private final Long postId;

    private final String postType;

    private final String title;

    private final String content;

    private final MemberResponse author;

    private final String category;

    private final String location;

    private final Instant createdTime;


    public CommunityPostResponse(Long postId, String title, String content,
            MemberResponse author, String category, String location,
            Instant createdTime) {
        this.postId = postId;
        this.postType = "community";
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.location = location;
        this.createdTime = createdTime;
    }

    public static CommunityPostResponse of(CommunityPost communityPost) {
        Member author = communityPost.getMember();

        Category category = communityPost.getCategory();
        String categoryName = null;
        if (category != null) {
            categoryName = category.getName();
        }

        Location location = communityPost.getLocation();
        String locationName = null;
        if (location != null) {
            locationName = location.getName();
        }

        return new CommunityPostResponse(communityPost.getId(), communityPost.getTitle(),
                communityPost.getContent(),
                MemberResponse.of(author), categoryName, locationName,
                communityPost.getCreatedTime());
    }
}
