package com.parksupark.soomjae.server.community.participation.repository;

import com.parksupark.soomjae.server.community.participation.entity.Participation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    long countByMeetingPostId(Long postId);

    List<Participation> findByMeetingPostId(Long postId);

    Optional<Participation> findByMeetingPostIdAndParticipantId(Long postId, Long memberId);

    boolean existsByMeetingPostIdAndParticipantId(Long postId, Long memberId);
}
