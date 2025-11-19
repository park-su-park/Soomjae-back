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
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String bio;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true,
        fetch = FetchType.LAZY)
    private ProfileImage profileImage;

    private Profile(Member member) {
        this.member = member;
    }

    public static Profile create(Member member) {

        return new Profile(member);
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
        profileImage.setProfile(this);
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImage.updateImageUrl(profileImageUrl);
    }
}
