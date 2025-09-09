package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.dto.MemberBasicInfo;
import com.parksupark.soomjae.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    Optional<MemberBasicInfo> findBasicInfoById(Long id);


    Optional<MemberBasicInfo> findBasicInfoByEmail(String email);
}
