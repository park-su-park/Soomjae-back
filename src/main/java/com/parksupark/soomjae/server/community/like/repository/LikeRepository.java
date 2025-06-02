package com.parksupark.soomjae.server.community.like.repository;

import com.parksupark.soomjae.server.community.like.entity.Like;
import java.util.Optional;

public interface LikeRepository {

    Optional<Like> findByPostTypeAndPostIdAndMemberId(String postType, Long postId, Long memberId);

    Long countByPostTypeAndPostId(String postType, Long postId);

    boolean existsByPostTypeAndPostIdAndMemberId(String postType, Long postId, Long memberId);

    Like save(Like like);

    void delete(Like like);
}
