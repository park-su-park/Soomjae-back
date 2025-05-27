package com.parksupark.soomjae.server.auth.username.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.username.filter.UsernamePasswordLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class UsernamePasswordSecurityConfig {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /**
     * <b>Why CSRF is disabled:</b>
     * <ul>
     *     <li>CSRF는 세션 기반 인증(stateful)에 대한 공격으로, JWT 기반 stateless 인증에는 의미 없음</li>
     *     <li>불필요한 403 오류와 CSRF 토큰 처리 방지를 위해 disable함</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        AuthenticationManager authenticationManager, AuthenticationProvider authenticationProvider)
        throws Exception {

        UsernamePasswordLoginFilter usernamePasswordLoginFilter = new UsernamePasswordLoginFilter(
            authenticationManager, objectMapper, jwtProvider);

        http
            .csrf(csrf -> csrf.disable())

            .formLogin(form -> form.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/v1/create-member").permitAll()
                .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider)

            .authenticationManager(authenticationManager)

            .addFilterAt(usernamePasswordLoginFilter, UsernamePasswordAuthenticationFilter.class);

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

}