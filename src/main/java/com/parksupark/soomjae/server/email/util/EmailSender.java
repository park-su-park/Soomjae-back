package com.parksupark.soomjae.server.email.util;

import java.util.concurrent.CompletableFuture;

public interface EmailSender {

    CompletableFuture<Void> sendVerificationEmail(String email, String code);
}
