package com.parksupark.soomjae.server.community.post.communitypost.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.Getter;

@Getter
public class CommunityPostRequest {

    private String title;
    private String content;
    private String category;
    private String location;

    public CommunityPost toEntity(Member member, Category category, Location location) {
        return CommunityPost.builder()
            .title(title)
            .content(content)
            .member(member)
            .category(category)
            .location(location)
            .build();
    }
}
