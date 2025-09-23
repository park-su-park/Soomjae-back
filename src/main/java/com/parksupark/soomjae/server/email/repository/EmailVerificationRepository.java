package com.parksupark.soomjae.server.email.repository;

import com.parksupark.soomjae.server.email.entity.EmailVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);
}
