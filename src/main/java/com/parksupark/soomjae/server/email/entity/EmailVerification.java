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

    private static final int EMAIL_MAX_LENGTH = 255;
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_SECONDS = 300;

    @Id
    @Column(length = EMAIL_MAX_LENGTH)
    private String email;

    @Column(length = CODE_LENGTH, nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expirationTime;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;

    private EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.expirationTime = Instant.now().plusSeconds(EXPIRATION_SECONDS);
    }

    public static EmailVerification create(String email, String code) {
        return new EmailVerification(email, code);
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
