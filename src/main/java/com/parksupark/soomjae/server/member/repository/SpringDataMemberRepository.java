package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.dto.MemberBasicInfo;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<MemberBasicInfo> findBasicInfoById(Long id);

    @Override
    Optional<MemberBasicInfo> findBasicInfoByEmail(String email);
}
