package com.parksupark.soomjae.server.auth.username.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.exception.FilterAuthenticationFailedException;
import com.parksupark.soomjae.server.auth.common.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class UsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    public UsernamePasswordLoginFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtProvider = jwtProvider;
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            UsernamePasswordLoginRequest loginRequest = objectMapper.readValue(request.getReader(),
                UsernamePasswordLoginRequest.class);

            String principal = loginRequest.getEmail();
            String credential = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, credential);

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new FilterAuthenticationFailedException(
                "error occurred while processing username/password authorization", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        UsernamePasswordUserDetails principal = (UsernamePasswordUserDetails) authResult.getPrincipal();

        String username = principal.getUsername();
        Role role = principal.getMember().getRole();

        String token = jwtProvider.generateToken(username, role);

        UsernamePasswordAuthSuccessResponse successResponse =
            new UsernamePasswordAuthSuccessResponse(token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), successResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        log.warn("username/password login failed: {}", failed.toString());

        // 로그인 실패 응답 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 응답 바디는 추후 구현
    }
}
