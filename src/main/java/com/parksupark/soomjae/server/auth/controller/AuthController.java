package com.parksupark.soomjae.server.auth.controller;

import com.parksupark.soomjae.server.auth.common.exception.RefreshFailedException;
import com.parksupark.soomjae.server.auth.service.AuthService;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<UsernamePasswordAuthSuccessResponse> refresh(HttpServletRequest request) {

        UsernamePasswordAuthSuccessResponse response =
                authService.refresh(extractRefreshTokenFromCookie(request));

        return ResponseEntity.ok(response);
    }



    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new RefreshFailedException(
                    ErrorMessages.REFRESH_TOKEN_NOT_FOUND_FROM_COOKIE_MESSAGE);
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RefreshFailedException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND_FROM_COOKIE_MESSAGE);
    }

}

