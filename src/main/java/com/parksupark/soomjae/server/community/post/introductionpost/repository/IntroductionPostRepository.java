package com.parksupark.soomjae.server.community.post.introductionpost.repository;

import com.parksupark.soomjae.server.community.post.introductionpost.entity.IntroductionPost;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionPostRepository extends JpaRepository<IntroductionPost, Long> {

    boolean existsByMember(Member member);

    Optional<IntroductionPost> findByMemberId(Long memberId);

}
