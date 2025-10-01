package com.parksupark.soomjae.server.email.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface EmailVerificationService {

    DeferredResult<ResponseEntity<Void>> sendVerificationCode(String email);

    void verifyCode(String email, String code);

}
