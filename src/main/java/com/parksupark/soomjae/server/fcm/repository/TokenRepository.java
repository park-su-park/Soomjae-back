package com.parksupark.soomjae.server.fcm.repository;

import com.parksupark.soomjae.server.fcm.domain.Token;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenValueAndMember(String value, Member member);

    List<Token> findByMemberInAndExpirationDateAfter(List<Member> members, LocalDate date);

    @Modifying
    @Query("DELETE FROM Token t WHERE t IN :failedTokens")
    void deleteByTokenIn(@Param("failedTokens") List<Token> failedTokens);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.tokenValue IN :failedTokens")
    void deleteByTokenValueIn(@Param("failedTokens") List<String> failedTokens);

    List<Token> findByExpirationDate(LocalDate now);
}
