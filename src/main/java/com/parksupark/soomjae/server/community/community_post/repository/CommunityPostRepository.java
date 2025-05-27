package com.parksupark.soomjae.server.community.community_post.repository;

import com.parksupark.soomjae.server.community.community_post.entity.CommunityPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByMemberId(Long memberId);
}

