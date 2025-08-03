package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.Instant;
import lombok.Getter;

@Getter
public class MeetingPostRequest {

    private String title;

    private String content;

    private String category;

    private String location;

    private int maximumParticipants;

    private Instant startTime;

    private Instant endTime;

    public MeetingPost toEntity(Member member, Category category, Location location) {
        return MeetingPost.builder()
            .title(title)
            .content(content)
            .category(category)
            .location(location)
            .endTime(endTime)
            .startTime(startTime)
            .maximumParticipants(maximumParticipants)
            .member(member)
            .build();
    }
}
