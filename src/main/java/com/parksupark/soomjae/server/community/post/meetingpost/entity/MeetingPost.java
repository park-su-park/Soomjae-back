package com.parksupark.soomjae.server.community.post.meetingpost.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeetingPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    private String title;
    private String content;

    private int maximumParticipants;

    private Instant startTime;

    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MeetingStatus status = MeetingStatus.OPEN;
}