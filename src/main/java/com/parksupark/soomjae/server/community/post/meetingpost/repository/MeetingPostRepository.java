package com.parksupark.soomjae.server.community.post.meetingpost.repository;

import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostStatsResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {

    Page<MeetingPost> findByMemberId(Long memberId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MeetingPost m WHERE m.id = :id")
    Optional<MeetingPost> findByIdForUpdate(@Param("id") Long id);

    @Query("""
        SELECT new
        com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostStatsResponse(
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
        LEFT JOIN likes lm ON lm.postId = m.id AND lm.postType = 'meeting' 
        AND lm.member.id = :memberId
        WHERE m.id IN :postIds
        GROUP BY m.id
        """)
    List<MeetingPostStatsResponse> findPostStats(@Param("postIds") List<Long> postIds,
        @Param("memberId") Long memberId);
}

