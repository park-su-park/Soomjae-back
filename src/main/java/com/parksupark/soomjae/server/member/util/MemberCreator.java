package com.parksupark.soomjae.server.member.util;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberCreator {

    private final MemberRepository memberRepository;
    private final RandomNicknameCreator randomNicknameCreator;

    @Retryable(
        retryFor = {DataIntegrityViolationException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 50)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member createLocalMember(String email, String encodedPassword) {
        String nickname = generateUniqueNickname();
        Member member = Member.create(email, encodedPassword, nickname);
        return memberRepository.save(member);
    }

    @Retryable(
        retryFor = {DataIntegrityViolationException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 50)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member createOAuthMember(String email, AuthProvider provider, String providerId) {
        String nickname = generateUniqueNickname();
        Member member = Member.createOAuthMember(email, provider, nickname, providerId);
        return memberRepository.save(member);
    }

    // 닉네임 중복을 피하는 1차 방어 로직
    private String generateUniqueNickname() {
        String randomNickname;
        do {
            randomNickname = randomNicknameCreator.createRandomNickname();
        } while (memberRepository.existsByNickname(randomNickname));
        return randomNickname;
    }
}
