package com.parksupark.soomjae.server.community.post.memberpost.entity;

import com.parksupark.soomjae.server.image.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPostImage extends Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_post_id")
    private MemberPost memberPost;

    private MemberPostImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static MemberPostImage create(String imageUrl) {
        return new MemberPostImage(imageUrl);
    }

    void setMemberPost(MemberPost memberPost) {
        this.memberPost = memberPost;
    }
}