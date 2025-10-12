package com.parksupark.soomjae.server.email.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_email_code_expiration", columnList = "email, code, expirationTime"),
    @Index(name = "idx_email_verified_expiration", columnList = "email, verified, expirationTime")
})
public class EmailVerification extends BaseEntity {

    private static final int EMAIL_MAX_LENGTH = 255;
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_SECONDS = 60 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = EMAIL_MAX_LENGTH, nullable = false)
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
