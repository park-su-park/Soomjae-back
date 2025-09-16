package com.parksupark.soomjae.server.community.post.meetingpost.repository;

import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {

    Page<MeetingPost> findByMemberId(Long memberId, Pageable pageable);
}

