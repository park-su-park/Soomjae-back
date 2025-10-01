package com.parksupark.soomjae.server.email.repository;

import com.parksupark.soomjae.server.email.entity.EmailVerification;
import java.time.Instant;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    void deleteByEmail(String email);

    @Query("SELECT e FROM EmailVerification e WHERE e.email = :email AND e.expirationTime > :now ")
    Optional<EmailVerification> findByEmailAndExpiredAtAfter(@Param("email") String email,
        @Param("now") Instant now);

    @Query(
        """
            SELECT e
            FROM EmailVerification e
            WHERE e.email = :email AND e.code = :code AND e.expirationTime > :now
            """)
    Optional<EmailVerification> findByEmailAndCodeAndExpirationTimeAfter(@Param("email") String email,
        @Param("code") String code, @Param("now") Instant now);

    @Query(
        """
                SELECT COUNT(e) > 0
                FROM EmailVerification e
                WHERE e.email = :email AND e.verified = TRUE AND e.expirationTime > :now
            """)
    boolean existsByEmailAndVerifiedAndExpirationTimeAfter(@Param("email") String email,
        @Param("now") Instant now);
}
