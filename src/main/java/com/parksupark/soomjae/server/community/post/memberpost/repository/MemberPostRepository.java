package com.parksupark.soomjae.server.community.post.memberpost.repository;

import com.parksupark.soomjae.server.community.post.memberpost.entity.MemberPost;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberPostRepository extends JpaRepository<MemberPost, Long> {

    @Query("SELECT mp FROM MemberPost mp JOIN FETCH mp.member WHERE mp.id = :id")
    Optional<MemberPost> findByIdWithMember(@Param("id") Long id);
}
