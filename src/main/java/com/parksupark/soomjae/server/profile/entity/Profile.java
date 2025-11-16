package com.parksupark.soomjae.server.profile.entity;

import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.profile.dto.UpdateProfileRequest;
import jakarta.persistence.CascadeType;
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
public class Profile {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true,
        fetch = FetchType.LAZY)
    private ProfileImage profileImage;

    @Column(nullable = false, unique = true)
    private String nickname;

    private Profile(String bio, Member member, String nickname) {
        this.bio = bio;
        this.member = member;
        this.nickname = nickname;
    }

    public static Profile create(String bio, Member member, String nickname) {

        return new Profile(bio, member, nickname);
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
        profileImage.setProfile(this);
    }

    public void updateProfile(UpdateProfileRequest request) {
        this.profileImage.updateImageUrl(request.getProfileImageUrl());
        this.bio = request.getBio();
        this.nickname = request.getNickname();
    }
}
