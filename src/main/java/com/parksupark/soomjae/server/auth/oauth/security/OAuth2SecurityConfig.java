package com.parksupark.soomjae.server.auth.oauth.security;

import com.parksupark.soomjae.server.auth.oauth.handler.OAuth2FailureHandler;
import com.parksupark.soomjae.server.auth.oauth.handler.OAuth2SuccessHandler;
import com.parksupark.soomjae.server.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oauth2Successhandler;
    private final OAuth2FailureHandler oauth2Failurehandler;

    /**
     * <b>Why CSRF is disabled:</b>
     * <ul>
     *     <li>OAuth2에서는 state 파라미터가 CSRF 보호 역할</li>
     *     <li>JWT 기반 stateless 인증에는 CSRF가 불필요</li>
     * </ul>
     */
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/oauth2/**", "/login/oauth2/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .successHandler(oauth2Successhandler)
                .failureHandler(oauth2Failurehandler)
            );

        return http.build();
    }
}
