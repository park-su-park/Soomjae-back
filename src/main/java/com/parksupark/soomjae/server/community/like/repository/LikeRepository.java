package com.parksupark.soomjae.server.community.like.repository;

import com.parksupark.soomjae.server.community.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostTypeAndPostIdAndMemberId(String postType, Long postId, Long memberId);
    Long countByPostTypeAndPostId(String postType, Long postId);
    boolean existsByPostTypeAndPostIdAndMemberId(String postType, Long postId, Long memberId);
}
