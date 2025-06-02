package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
}
