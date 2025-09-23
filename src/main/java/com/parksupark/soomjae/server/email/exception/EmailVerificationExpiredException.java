package com.parksupark.soomjae.server.email.exception;

public class EmailVerificationExpiredException extends RuntimeException {

    public EmailVerificationExpiredException(String message) {
        super(message);
    }

}
