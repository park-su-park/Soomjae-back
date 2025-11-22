package com.parksupark.soomjae.server.community.post.introducepost.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.community.post.introducepost.dto.CreateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.dto.UpdateIntroducePostRequest;
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
public class IntroducePost extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    private IntroducePost(String content) {
        this.content = content;
    }

    public static IntroducePost create(CreateIntroducePostRequest request) {
        return new IntroducePost(request.getContent());
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateIntroducePost(UpdateIntroducePostRequest request) {
        this.content = request.getContent();
    }
}
