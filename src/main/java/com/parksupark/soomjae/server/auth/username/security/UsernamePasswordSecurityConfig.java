package com.parksupark.soomjae.server.auth.username.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UsernamePasswordSecurityConfig {

    /**
     * Configures the security filter chain for username-password based authentication.
     *
     * <p><b>Why CSRF is disabled:</b></p>
     * <ul>
     *     <li>CSRF는 세션 기반 인증(stateful)에 대한 공격으로, JWT 기반 stateless 인증에는 의미 없음</li>
     *     <li>불필요한 403 오류와 CSRF 토큰 처리 방지를 위해 disable함</li>
     * </ul>
     *
     * <p><b>Security settings overview:</b></p>
     * <ul>
     *     <li>Disables CSRF protection</li>
     *     <li>Sets session creation policy to {@code STATELESS}</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}