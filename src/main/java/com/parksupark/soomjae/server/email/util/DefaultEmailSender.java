package com.parksupark.soomjae.server.email.util;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.email.exception.EmailVerificationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String SUBJECT = "[숨재] 회원가입 인증코드";
    private static final String TEMPLATE_PATH = "email/verification";

    @Async
    @Override
    public CompletableFuture<Void> sendVerificationEmail(String email, String code) {
        try {
            log.info("thread name: {}", Thread.currentThread().getName());
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(SUBJECT);

            Context context = new Context();
            context.setVariable("verificationCode", code);
            context.setVariable("expirationMinutes", 60);

            String htmlContent = templateEngine.process(TEMPLATE_PATH, context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

            return CompletableFuture.completedFuture(null);

        } catch (MessagingException e) {
            throw new EmailVerificationFailedException(ErrorMessages.EMAIL_SEND_FAILED_MESSAGE);
        }
    }

}
