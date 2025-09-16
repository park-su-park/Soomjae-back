package com.parksupark.soomjae.server.auth.jwt.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // 디바이스 당 하나의 refresh token 만을 허용
    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    private RefreshToken(String token, Long memberId, Instant expiresAt) {
        this.token = token;
        this.memberId = memberId;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(String token, Long memberId, Instant expiresAt) {
        return new RefreshToken(token, memberId, expiresAt);
    }
}
