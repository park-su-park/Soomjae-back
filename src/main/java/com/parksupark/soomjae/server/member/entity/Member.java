package com.parksupark.soomjae.server.member.entity;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.member.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "member",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "provider"})
    }
)
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Member(String email, String password, Role role, AuthProvider provider,
        String nickname, String providerId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static Member create(String email, String password, String nickname) {
        return new Member(email, password, Role.USER, AuthProvider.LOCAL, nickname, null);
    }

    public static Member createOAuthMember(String email, AuthProvider provider,
        String nickname, String providerId) {
        return new Member(email, null, Role.USER, provider, nickname, providerId);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
