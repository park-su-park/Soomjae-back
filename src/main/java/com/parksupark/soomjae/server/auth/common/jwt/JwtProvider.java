package com.parksupark.soomjae.server.auth.common.jwt;

import com.parksupark.soomjae.server.member.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private final JwtHandler jwtHandler;


    public String generateToken(String subject, Role role) {
        return jwtHandler.generate(subject, role);
    }

    /**
     * <p>JWT 토큰에서 Authentication 객체를 복원.</p>
     * <p>현재는 항상 UserDetailsService를 통해 사용자 정보를
     * 조회하여 실시간으로 최신 권한(Role)을 반영함.</p>
     * <p>JWT 내 role claim은 현재 사용하지 않지만,</p>
     * <b>추후 성능 향상을 위해 JWT의 role claim을 통해 권한을 복원하는 방식으로 확장할 수 있음.</b>
     * <p>즉, 현 단계에서는 role 정보를 claim에 포함하되,
     * 인증 객체의 권한은 항상 DB 기준으로 설정함.</p>
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims getClaimsFromToken(String token) {
        return jwtHandler.parse(token);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("expired JWT token: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("invalid JWT token: {}", e.getMessage());
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
