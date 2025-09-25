package com.parksupark.soomjae.server.email.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.email.entity.EmailVerification;
import com.parksupark.soomjae.server.email.exception.EmailVerificationFailedException;
import com.parksupark.soomjae.server.email.repository.EmailVerificationRepository;
import com.parksupark.soomjae.server.email.util.SecureCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class DefaultEmailVerificationService implements EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final SecureCodeGenerator codeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;
    private final SpringTemplateEngine templateEngine;

    private static final String subject = "[숨재] 회원가입 인증코드";

    @Override
    @Transactional
    public void sendVerificationCode(String email) {

        String code = codeGenerator.generateVerificationCode();

        emailVerificationRepository.deleteByEmail(email);

        emailVerificationRepository.save(EmailVerification.create(email, code));

        sendVerificationEmail(email, subject, code);
    }

    @Override
    @Transactional
    public void verifyCode(String email, String code) {
        EmailVerification emailVerification = findValidVerification(email);

        String normalizedCode = code.toUpperCase().trim();

        if (!normalizedCode.equals(emailVerification.getCode())) {
            throw new EmailVerificationFailedException(
                ErrorMessages.EMAIL_VERIFICATION_WRONG_CODE_MESSAGE);
        }

        emailVerification.setVerified(true);
    }

    private void sendVerificationEmail(String email, String subject, String code) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("verificationCode", code);
            context.setVariable("expirationMinutes", 5);

            String htmlContent = templateEngine.process("email/verification", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailVerificationFailedException(ErrorMessages.EMAIL_SEND_FAILED_MESSAGE);
        }
    }

    private EmailVerification findValidVerification(String email) {
        return emailVerificationRepository.findByEmailAndExpiredAtAfter(email, Instant.now())
            .orElseThrow(() -> new ResourceNotFoundException(
                ErrorMessages.EMAIL_VERIFICATION_NOT_FOUND_MESSAGE));
    }
}
