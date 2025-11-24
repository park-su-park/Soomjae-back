package com.parksupark.soomjae.server.fcm.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.parksupark.soomjae.server.fcm.domain.Token;
import com.parksupark.soomjae.server.fcm.dto.AlarmDto;
import com.parksupark.soomjae.server.fcm.repository.TokenRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final TokenRepository tokenRepository;

    public void sendByAlarm(AlarmDto alarmDto, List<Token> tokens) {
        log.info("알림 전송 메소드 시작");

        if (tokens == null || tokens.isEmpty()) {
            log.info("보낼 FCM 토큰이 없습니다. alarmDto={}", alarmDto);
            return;
        }

        List<String> tokenValues = tokens.stream()
            .map(Token::getTokenValue)
            .toList();

        com.google.firebase.messaging.MulticastMessage message =
            com.google.firebase.messaging.MulticastMessage.builder()
                .addAllTokens(tokenValues)
                .setNotification(Notification.builder()
                    .setTitle(alarmDto.getTitle())
                    .setBody(alarmDto.getContent())
                    .setImage(alarmDto.getImg())
                    .build())
                .putData("click_action", alarmDto.getUrl())
                .build();

        try {
            BatchResponse batchResponse =
                FirebaseMessaging.getInstance().sendEachForMulticast(message);

            List<Token> tokensToDelete = new ArrayList<>();

            for (int i = 0; i < batchResponse.getResponses().size(); i++) {
                SendResponse response = batchResponse.getResponses().get(i);

                if (!response.isSuccessful()) {
                    Exception ex = response.getException();

                    log.error("FCM 전송 실패 token: {}", tokens.get(i).getTokenValue(), ex);

                    if (ex instanceof FirebaseMessagingException fme) {
                        if (shouldDeleteToken(fme)) {
                            tokensToDelete.add(tokens.get(i));
                        } else {
                            // 삭제는 안 하지만, 어떤 코드였는지는 로그로 남겨두자
                            log.warn("토큰은 유지. FCM 에러 코드={}, message={}",
                                fme.getMessagingErrorCode(), fme.getMessage());
                        }
                    }
                } else {
                    Token successToken = tokens.get(i);
                    successToken.setLastUsed(Instant.now());
                }
            }

            if (!tokensToDelete.isEmpty()) {
                log.warn("유효하지 않은 토큰 삭제. count={}, tokens={}",
                    tokensToDelete.size(), tokensToDelete);
                tokenRepository.deleteByTokenIn(tokensToDelete);
            }

        } catch (FirebaseMessagingException e) {
            log.error("sendEachForMulticast 실행 중 예외 발생", e);
        }
    }

    /**
     * 이 에러라면 토큰을 DB에서 삭제해도 되는가?
     */
    private boolean shouldDeleteToken(FirebaseMessagingException fme) {
        MessagingErrorCode code = fme.getMessagingErrorCode();
        String message = fme.getMessage() != null ? fme.getMessage() : "";

        // 1) UNREGISTERED: 더 이상 사용 불가 → 무조건 삭제
        if (code == MessagingErrorCode.UNREGISTERED) {
            return true;
        }

        // 2) INVALID_ARGUMENT 중에서도 "Invalid registration" / "Missing registration" 인 경우만 삭제
        if (code == MessagingErrorCode.INVALID_ARGUMENT) {
            if (message.contains("Invalid registration")
                || message.contains("Missing registration")) {
                return true;
            }
            // 그 외 INVALID_ARGUMENT는 payload/옵션 문제일 수 있으므로 삭제 X
            return false;
        }

        // 3) SENDER_ID_MISMATCH: 우리 프로젝트에선 다시 성공할 수 없다고 보고 삭제
        if (code == MessagingErrorCode.SENDER_ID_MISMATCH) {
            return true;
        }

        // QUOTA_EXCEEDED, UNAVAILABLE, INTERNAL, THIRD_PARTY_AUTH_ERROR 등은 삭제 X
        return false;
    }

}
