package com.parksupark.soomjae.server.community.post.memberpost.repository;

import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostGridProjection;
import com.parksupark.soomjae.server.community.post.memberpost.entity.MemberPost;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberPostRepository extends JpaRepository<MemberPost, Long> {

    @Query("SELECT mp FROM MemberPost mp JOIN FETCH mp.member WHERE mp.id = :id")
    Optional<MemberPost> findByIdWithMember(@Param("id") Long id);

    @Query("""
        SELECT new com.parksupark.soomjae.server.community.post.memberpost.dto
        .MemberPostGridProjection(
          p.id,
          i.imageUrl
        )
        FROM MemberPost p
        LEFT JOIN MemberPostImage i ON i.memberPost = p AND i.id=(
          SELECT MIN(sub_i.id)
          FROM MemberPostImage sub_i
          WHERE sub_i.memberPost =p
        )
        WHERE p.member.id = :memberId
        ORDER BY p.createdTime DESC 
        """)
    Page<MemberPostGridProjection> findMemberPostGridByMemberId(@Param("memberId") Long memberId,
        Pageable pageable);

    Page<MemberPost> findAllByOrderByCreatedTimeDesc(Pageable pageable);
}
