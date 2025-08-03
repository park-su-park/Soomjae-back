package com.parksupark.soomjae.server.community.comment.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member member;

    private String postType;

    private Long postId;

    private String content;

    @Builder
    public Comment(Member member, String postType, Long postId, String content) {
        this.member = member;
        this.postType = postType;
        this.postId = postId;
        this.content = content;
    }
}
