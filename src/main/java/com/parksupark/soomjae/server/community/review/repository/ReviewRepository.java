package com.parksupark.soomjae.server.community.review.repository;

import com.parksupark.soomjae.server.community.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByParticipationId(Long participationId);
}
