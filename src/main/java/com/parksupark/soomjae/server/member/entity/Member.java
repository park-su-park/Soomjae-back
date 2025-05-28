package com.parksupark.soomjae.server.member.entity;

import com.parksupark.soomjae.server.member.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member create(String email, String password) {
        return new Member(email, password, Role.USER);
    }

}
