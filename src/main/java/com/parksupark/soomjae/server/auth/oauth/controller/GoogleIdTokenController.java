package com.parksupark.soomjae.server.auth.oauth.controller;

import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.auth.oauth.dto.GoogleIdTokenVerificationRequest;
import com.parksupark.soomjae.server.auth.oauth.dto.GoogleIdTokenVerificationResponse;
import com.parksupark.soomjae.server.auth.oauth.dto.GoogleIdTokenVerificationResult;
import com.parksupark.soomjae.server.auth.oauth.service.GoogleIdTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oauth2/google")
@RequiredArgsConstructor
public class GoogleIdTokenController {

    private final GoogleIdTokenService googleIdTokenService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @PostMapping("/id-token")
    public ResponseEntity<GoogleIdTokenVerificationResponse> verifyIdToken(
        @RequestBody @Valid GoogleIdTokenVerificationRequest request,
        HttpServletResponse response
    ) {
        String idTokenString = request.getIdToken();

        GoogleIdTokenVerificationResult result = googleIdTokenService.verify(idTokenString);

        setCookie(response, result.getRefreshToken());

        GoogleIdTokenVerificationResponse googleIdTokenVerificationResponse =
            new GoogleIdTokenVerificationResponse(result.getAccessToken(),
                result.getMember().getId());

        return ResponseEntity.ok(googleIdTokenVerificationResponse);

    }

    private void setCookie(HttpServletResponse response, RefreshToken refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Strict")
            .maxAge(Duration.between(Instant.now(), refreshToken.getExpiresAt()))
            .path("/")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
