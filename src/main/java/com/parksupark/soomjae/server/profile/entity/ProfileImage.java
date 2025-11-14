package com.parksupark.soomjae.server.profile.entity;

import com.parksupark.soomjae.server.image.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ProfileImage extends Image {

    @Id
    @GeneratedValue
    @Column(name = "profile_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private ProfileImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ProfileImage create(String imageUrl) {
        return new ProfileImage(imageUrl);
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }
}
