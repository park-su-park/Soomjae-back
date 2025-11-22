package com.parksupark.soomjae.server.community.post.introducepost.repository;

import com.parksupark.soomjae.server.community.post.introducepost.entity.IntroducePost;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroducePostRepository extends JpaRepository<IntroducePost, Long> {

    boolean existsByMember(Member member);

    Optional<IntroducePost> findByMemberId(Long memberId);

}
