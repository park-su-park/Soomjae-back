package com.parksupark.soomjae.server.auth.jwt.repository;

import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
}
