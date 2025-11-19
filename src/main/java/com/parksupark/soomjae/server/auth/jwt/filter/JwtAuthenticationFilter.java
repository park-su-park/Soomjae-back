package com.parksupark.soomjae.server.auth.jwt.filter;

import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = jwtProvider.resolveToken(request);

        if (token != null) {
            try {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                setErrorResponse(response, ErrorMessages.ACCESS_TOKEN_EXPIRED_MESSAGE);
                return; // 필터체인 중단
            } catch (JwtException | IllegalArgumentException e) {
                // 유효하지 않은 access token일 경우 처리
                setErrorResponse(response, ErrorMessages.INVALID_ACCESS_TOKEN_MESSAGE);
                return; // 필터체인 중단
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(message);
    }
}
