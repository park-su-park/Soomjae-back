package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.member.dto.MemberBasicInfo;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    void delete(Member member);

    void flush();

    boolean existsById(Long id);

    boolean existsByEmailAndProvider(String email, AuthProvider provider);

    Optional<Member> findByProviderAndProviderId(AuthProvider provider, String providerId);

    default Optional<MemberBasicInfo> findBasicInfoById(Long id) {
        return Optional.empty();
    }

    default Optional<MemberBasicInfo> findBasicInfoByEmail(String email) {
        return Optional.empty();
    }
}
