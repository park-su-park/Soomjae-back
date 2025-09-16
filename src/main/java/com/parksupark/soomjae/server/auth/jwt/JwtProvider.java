package com.parksupark.soomjae.server.auth.jwt;

import com.parksupark.soomjae.server.auth.jwt.access.AccessTokenGenerator;
import com.parksupark.soomjae.server.auth.jwt.access.AccessTokenParser;
import com.parksupark.soomjae.server.auth.jwt.refresh.RefreshTokenGenerator;
import com.parksupark.soomjae.server.auth.jwt.refresh.RefreshTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsService userDetailsService;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final AccessTokenParser accessTokenParser;
    private final RefreshTokenParser refreshTokenParser;


    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return accessTokenGenerator.generate(subject, claims);
    }

    public String generateRefreshToken(String subject) {
        return refreshTokenGenerator.generate(subject);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromAccessToken(token);
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    public Claims getClaimsFromAccessToken(String token) {
        return accessTokenParser.parse(token);
    }

    public Claims getClaimsFromRefreshToken(String token) {
        return refreshTokenParser.parse(token);
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = getClaimsFromAccessToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("expired JWT token: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromRefreshToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("expired JWT refresh token: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("invalid JWT refresh token: {}", e.getMessage());
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
