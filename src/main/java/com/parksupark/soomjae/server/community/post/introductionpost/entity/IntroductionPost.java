package com.parksupark.soomjae.server.community.post.introductionpost.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.community.post.introductionpost.dto.UpdateIntroductionPostRequest;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IntroductionPost extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String content;

    private IntroductionPost(String content) {
        this.content = content;
    }

    public static IntroductionPost create(String content) {
        return new IntroductionPost(content);
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateIntroductionPost(UpdateIntroductionPostRequest request) {
        this.content = request.getContent();
    }
}
