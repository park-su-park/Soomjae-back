package com.parksupark.soomjae.server.community.like.repository;

import com.parksupark.soomjae.server.community.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataLikeRepository extends JpaRepository<Like, Long>, LikeRepository {
}
