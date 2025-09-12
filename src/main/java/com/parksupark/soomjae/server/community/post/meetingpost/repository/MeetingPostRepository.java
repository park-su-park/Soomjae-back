package com.parksupark.soomjae.server.community.post.meetingpost.repository;

import com.parksupark.soomjae.server.community.post.meetingpost.dto.PostStatsResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {

    Page<MeetingPost> findByMemberId(Long memberId, Pageable pageable);

    @Query("""
        SELECT new com.parksupark.soomjae.server.community.post.meetingpost.dto.PostStatsResponse(
            m.id,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT l.id),
            COUNT(DISTINCT p.id),
            CASE WHEN COUNT(DISTINCT lm.id) > 0 THEN true ELSE false END
        )
        FROM MeetingPost m
        LEFT JOIN Comment c ON c.postId = m.id
        LEFT JOIN likes l ON l.postId = m.id AND l.postType = 'meeting'
        LEFT JOIN Participation p ON p.meetingPost.id = m.id
        LEFT JOIN likes lm ON lm.postId = m.id AND lm.postType = 'meeting' AND lm.member.id = :memberId
        WHERE m.id IN :postIds
        GROUP BY m.id
        """)
    List<PostStatsResponse> findPostStats(@Param("postIds") List<Long> postIds,
        @Param("memberId") Long memberId);
}

