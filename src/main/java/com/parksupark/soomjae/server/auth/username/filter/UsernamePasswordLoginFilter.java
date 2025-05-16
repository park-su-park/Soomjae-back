package com.parksupark.soomjae.server.auth.username.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.AuthenticationFailedException;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
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

    public UsernamePasswordLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UsernamePasswordLoginRequest loginRequest = objectMapper.readValue(request.getReader(),
                UsernamePasswordLoginRequest.class);

            String principal = loginRequest.getUsername();
            String credential = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, credential);

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new AuthenticationFailedException(
                "error occurred while processing username/password authorization", e);
        }
    }
}
