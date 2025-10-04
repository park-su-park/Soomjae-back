package com.parksupark.soomjae.server.email.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.email.entity.EmailVerification;
import com.parksupark.soomjae.server.email.repository.EmailVerificationRepository;
import com.parksupark.soomjae.server.email.util.EmailSender;
import com.parksupark.soomjae.server.email.util.SecureCodeGenerator;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public DeferredResult<ResponseEntity<?>> sendVerificationCode(String email) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);

        String code = codeGenerator.generateVerificationCode();

        CompletableFuture<Void> emailFuture = defaultEmailSender.sendVerificationEmail(email,
            code);

        emailFuture.orTimeout(DEFERRED_RESULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        Throwable cause = throwable.getCause() != null ? throwable.getCause() : throwable;

                        Map<String, String> body = Map.of("message", cause.getMessage());

                        ResponseEntity<Map<String, String>> errorResponse = ResponseEntity.status(
                                HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body);

                        deferredResult.setResult(errorResponse);
                    } else {
                        emailVerificationRepository.save(EmailVerification.create(email, code));

                        Map<String, String> body = Map.of("message", "success");
                        deferredResult.setResult(ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body));
                    }
                });

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
