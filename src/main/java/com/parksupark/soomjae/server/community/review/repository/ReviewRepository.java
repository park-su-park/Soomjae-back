package com.parksupark.soomjae.server.community.review.repository;

import com.parksupark.soomjae.server.community.review.entity.Review;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByParticipationId(Long participationId);

    @Query(
        """
            SELECT r.id
            FROM Review r
            WHERE r.participation.id = :participationId
            """
    )
    Optional<Long> findReviewIdByParticipationId(@Param("participationId") Long participationId);

}
