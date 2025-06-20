package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    void delete(Member member);

    void flush();

}
