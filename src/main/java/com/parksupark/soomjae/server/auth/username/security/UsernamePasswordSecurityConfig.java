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
     * <p><b>why csrf is disabled:</b></p>
     * <ul>
     *     <li>csrf는 세션 기반 인증(stateful) 방식에 사용 되는 공격 -> jwt 인증시(stateless) 효과 없음</li>
     *     <li>오작동 방지</li>
     * </ul>
     *
     * <p><b>Security settings overview:</b></p>
     * <ul>
     *     <li>Disables CSRF protection</li>
     *     <li>Sets session creation policy to {@code STATELESS}</li>
     * </ul>

     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}