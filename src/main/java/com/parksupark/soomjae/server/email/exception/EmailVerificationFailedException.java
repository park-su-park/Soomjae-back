package com.parksupark.soomjae.server.email.exception;

public class EmailVerificationFailedException extends RuntimeException {

    public EmailVerificationFailedException(String message) {
        super(message);
    }

}
