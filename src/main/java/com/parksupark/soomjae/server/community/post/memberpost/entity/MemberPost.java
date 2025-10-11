package com.parksupark.soomjae.server.community.post.memberpost.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToMany(mappedBy = "memberPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberPostImage> images = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    private MemberPost(Member member, String content) {
        this.member = member;
        this.content = content;
    }

    public static MemberPost create(Member member, String content) {
        return new MemberPost(member, content);
    }

    public void addImage(MemberPostImage image) {
        this.images.add(image);
        image.setMemberPost(this);
    }

    public void update(String content, List<String> imageUrls) {
        this.content = content;

        this.images.clear();
        if (imageUrls != null) {
            imageUrls.forEach(imageUrl -> {
                MemberPostImage image = MemberPostImage.create(imageUrl);
                this.addImage(image);
            });
        }
    }

}
