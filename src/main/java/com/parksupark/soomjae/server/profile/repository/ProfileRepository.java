package com.parksupark.soomjae.server.profile.repository;

import com.parksupark.soomjae.server.profile.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByMemberId(Long memberId);

    Optional<Profile> findByMemberId(Long memberId);
}
