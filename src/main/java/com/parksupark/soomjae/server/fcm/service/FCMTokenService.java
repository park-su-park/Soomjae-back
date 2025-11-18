package com.parksupark.soomjae.server.fcm.service;

import com.parksupark.soomjae.server.fcm.domain.Token;
import com.parksupark.soomjae.server.fcm.dto.FCMTokenRequest;
import com.parksupark.soomjae.server.fcm.repository.TokenRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMTokenService {

    private final TokenRepository tokenRepository;

    // 토큰 만료 기간 상수 정의
    private static final int TOKEN_EXPIRATION_PERIOD = 2;

    // 토픽 구독 - 토픽 하나씩

    @Transactional
    public void saveFCMToken(Member member, FCMTokenRequest request) {
        log.info("saveFCMToken 메서드 호출");

        // token이 이미 있는지 체크
        Optional<Token> existingToken = tokenRepository.findByDeviceAndMember(request.getDevice(),
            member);
        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            log.info("이미 존재하는 토큰: " + existingToken.get().getTokenValue());
            token.setLastUsed(Instant.now());
            token.setTokenValue(request.getFcmToken());
            tokenRepository.save(token);
        } else {
            // Only create and save a new token if it does not exist
            Token token = Token.builder().tokenValue(request.getFcmToken())
                .device(request.getDevice()).member(member).lastUsed(Instant.now()).build();
            log.info("DB에 저장하는 token : " + token.getTokenValue());
            tokenRepository.save(token);
        }
    }

    @Transactional
    public void delete(Member member, FCMTokenRequest request) {
        Optional<Token> byDeviceAndMemberAndTokenValue = tokenRepository
            .findByDeviceAndMemberAndTokenValue(request.getDevice(), member, request.getFcmToken());
        byDeviceAndMemberAndTokenValue.ifPresent(tokenRepository::delete);
    }

    // 매일 00:00(자정)에 트리거됩니다(0 0 0 * * ?). 따라서 하루에 한 번 작업이 실행됩니다.
    // 매일 자정에 실행되는 스케줄링 작업
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredTokens() {
        Instant deletePoint = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .minusMonths(TOKEN_EXPIRATION_PERIOD).toInstant();

        log.info("두달 동안 사용하지 않은 토큰을 모두 삭제합니다 : " + Instant.now());

        // 만료된 토큰을 가져옵니다.
        List<Token> expiredTokens = tokenRepository.findByLastUsedBefore(deletePoint);

        tokenRepository.deleteAll(expiredTokens);
    }

}
