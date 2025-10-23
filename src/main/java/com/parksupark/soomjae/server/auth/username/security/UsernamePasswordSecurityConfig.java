package com.parksupark.soomjae.server.auth.username.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.filter.JwtAuthenticationFilter;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.username.filter.UsernamePasswordLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("!test") // test 프로필이 아닐 때만 활성화
@EnableMethodSecurity(prePostEnabled = true)
public class UsernamePasswordSecurityConfig {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    /**
     * <b>Why CSRF is disabled:</b>
     * <ul>
     *     <li>CSRF는 세션 기반 인증(stateful)에 대한 공격으로, JWT 기반 stateless 인증에는 의미 없음</li>
     *     <li>불필요한 403 오류와 CSRF 토큰 처리 방지를 위해 disable함</li>
     * </ul>
     */
    @Bean
    @Order(3)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationManager authenticationManager,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        UsernamePasswordLoginFilter usernamePasswordLoginFilter = new UsernamePasswordLoginFilter(
            authenticationManager, objectMapper, jwtProvider,
            refreshTokenService, cookieSecure);

        http
            .securityMatcher("/v1/auth/login")
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterAt(usernamePasswordLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain publicEndpointsFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/v1/members/create-member", "/v1/auth/refresh")
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        AuthenticationProvider authenticationProvider) throws Exception {

        http
            .securityMatcher("/**")
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .authenticationProvider(authenticationProvider)
            .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }

}