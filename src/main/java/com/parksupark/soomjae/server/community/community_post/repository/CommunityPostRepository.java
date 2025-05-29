package com.parksupark.soomjae.server.community.community_post.repository;

import com.parksupark.soomjae.server.community.community_post.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    Page<CommunityPost> findByMemberId(Long memberId, Pageable pageable);
}

