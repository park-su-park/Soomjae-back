package com.parksupark.soomjae.server.email.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.email.entity.EmailVerification;
import com.parksupark.soomjae.server.email.exception.EmailVerificationFailedException;
import com.parksupark.soomjae.server.email.repository.EmailVerificationRepository;
import com.parksupark.soomjae.server.email.util.EmailSender;
import com.parksupark.soomjae.server.email.util.SecureCodeGenerator;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailVerificationService implements EmailVerificationService {

    private final SecureCodeGenerator codeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSender defaultEmailSender;

    private static final long DEFERRED_RESULT_TIMEOUT = 30 * 1000L;

    @Override
    @Transactional
    public DeferredResult<ResponseEntity<Void>> sendVerificationCode(String email) {
        log.info("thread name: {}", Thread.currentThread().getName());

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);

        deferredResult.onTimeout(() -> {
            throw new EmailVerificationFailedException(ErrorMessages.EMAIL_SEND_FAILED_MESSAGE);
        });

        String code = codeGenerator.generateVerificationCode();

        CompletableFuture<Void> emailFuture = defaultEmailSender.sendVerificationEmail(email,
            code);

        emailFuture.whenComplete((result, throwable) -> {
            // 비동기 작업에서 예외 발생 시
            if (throwable != null) {
                throw new EmailVerificationFailedException(ErrorMessages.EMAIL_SEND_FAILED_MESSAGE);
            } else {
                deferredResult.setResult(ResponseEntity.ok().build());
            }
        });

        emailVerificationRepository.save(EmailVerification.create(email, code));

        log.info("[{}] 요청 스레드 반납", Thread.currentThread().getName());
        return deferredResult;
    }

    @Override
    @Transactional
    public void verifyCode(String email, String code) {
        String normalizedCode = code.toUpperCase().trim();

        EmailVerification emailVerification = emailVerificationRepository.findByEmailAndCodeAndExpirationTimeAfter(
            email, normalizedCode,
            Instant.now()).orElseThrow(() -> new ResourceNotFoundException(
            ErrorMessages.EMAIL_VERIFICATION_NOT_FOUND_MESSAGE));

        emailVerification.setVerified(true);
    }
}
