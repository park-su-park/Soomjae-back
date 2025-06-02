package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    void delete(Member member);

}
