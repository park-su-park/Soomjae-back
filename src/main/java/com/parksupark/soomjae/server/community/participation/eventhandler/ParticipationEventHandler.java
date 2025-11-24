package com.parksupark.soomjae.server.community.participation.eventhandler;

import com.parksupark.soomjae.server.community.participation.entity.Participation;
import com.parksupark.soomjae.server.community.participation.repository.ParticipationRepository;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.ParticipationCreatedEvent;
import com.parksupark.soomjae.server.fcm.service.AlarmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ParticipationEventHandler {

    private final AlarmNotificationService alarmNotificationService;
    private final ParticipationRepository participationRepository;

    // 비동기 활성화 필요: @EnableAsync
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ParticipationCreatedEvent event) {
        Participation participation = participationRepository.findById(event.getParticipationId())
            .orElseThrow(); // 혹은 예외 처리

        alarmNotificationService.sendParticipationAlarm(participation);
    }
}
