package com.parksupark.soomjae.server.community.post.communitypost.repository;

import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostStatsResponse;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    Page<CommunityPost> findByMemberId(Long memberId, Pageable pageable);

    @Query("""
        SELECT new
        com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostStatsResponse(
            cp.id,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT l.id),
            CASE WHEN COUNT(DISTINCT lm.id) > 0 THEN true ELSE false END
        )
        FROM CommunityPost cp
        LEFT JOIN Comment c ON c.postId = cp.id
        LEFT JOIN likes l ON l.postId = cp.id AND l.postType = 'meeting'
        LEFT JOIN likes lm ON lm.postId = cp.id AND lm.postType = 'community' 
        AND lm.member.id = :memberId
        WHERE cp.id IN :postIds
        GROUP BY cp.id
        """)
    List<CommunityPostStatsResponse> findPostStats(@Param("postIds") List<Long> postIds,
        @Param("memberId") Long memberId);
}

