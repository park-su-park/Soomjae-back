package com.parksupark.soomjae.server.auth.common;

public class FilterAuthenticationFailedException extends RuntimeException {

    public FilterAuthenticationFailedException() {
        super("Authentication failed");
    }

    public FilterAuthenticationFailedException(String message) {
        super(message);
    }

    public FilterAuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterAuthenticationFailedException(Throwable cause) {
        super(cause);
    }
}
