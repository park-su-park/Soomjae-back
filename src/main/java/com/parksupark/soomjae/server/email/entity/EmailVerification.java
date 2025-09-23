package com.parksupark.soomjae.server.email.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification extends BaseEntity {

    @Id
    @Column(length = 255)
    private String email;

    @Column(length = 6, nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiredAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;

    private EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.expiredAt = Instant.now().plusSeconds(300);
    }

    public static EmailVerification create(String email, String code) {
        return new EmailVerification(email, code);
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
