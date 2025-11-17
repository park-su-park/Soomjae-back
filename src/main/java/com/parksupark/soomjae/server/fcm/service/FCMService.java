package com.parksupark.soomjae.server.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.parksupark.soomjae.server.fcm.domain.Token;
import com.parksupark.soomjae.server.fcm.dto.AlarmDto;
import com.parksupark.soomjae.server.fcm.repository.TokenRepository;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void sendByAlarm(AlarmDto alarmDto, List<Token> tokens) {

        List<Token> failedTokens = new ArrayList<>();

        for (Token token : tokens) {
            Message message = Message.builder()
                .setToken(token.getTokenValue())
                .setNotification(Notification.builder()
                    .setTitle(alarmDto.getTitle())
                    .setBody(alarmDto.getContent())
                    .setImage(alarmDto.getImg())
                    .build())
                .putData("click_action", alarmDto.getUrl())
                .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                failedTokens.add(token);
                log.error("Failed to send notification to token: " + token.getTokenValue(), e);
            }
        }

        if (!failedTokens.isEmpty()) {
            log.warn("유효하지 않은 토큰 목록 : " + failedTokens);
            tokenRepository.deleteByTokenIn(failedTokens);
        }
    }

}
