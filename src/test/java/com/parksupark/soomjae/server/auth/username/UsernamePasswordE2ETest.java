package com.parksupark.soomjae.server.auth.username;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>End-to-End 테스트 클래스.</b>
 *
 * <p>이 테스트의 목적은 실제 /auth/login 경로로 인증 요청이 들어왔을 때,
 * Spring Security의 전체 인증 흐름이 올바르게 작동하는지를 검증하는 것이다.</p>
 *
 * <p>테스트 대상 인증 흐름:</p>
 * <ul>
 *   <li>UsernamePasswordLoginFilter (Spring Security Filter)</li>
 *   <li>AuthenticationManager</li>
 *   <li>DaoAuthenticationProvider</li>
 *   <li>UserDetailsService</li>
 * </ul>
 *
 * <p>MockMvc를 통해 실제 HTTP 요청을 시뮬레이션하며,
 * 테스트용 application-test.properties에서 설정한 H2 메모리 DB를 기반으로
 *
 * @BeforeEach 단계에서 사용자 정보를 사전 등록한 뒤 인증을 수행한다.</p>
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class UsernamePasswordE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String username = "testuser";
    private final String password = "testpssword";

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.create(username, passwordEncoder.encode(password)));
    }


    @Test
    void usernamePasswordLogin_successfulFlow() throws Exception {

        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(username,
            password);


        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void usernamePasswordLogin_invalidUsername_unsuccessfulFlow() throws Exception {
        
        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(
            "wrongusername",
            password);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void usernamePasswordLogin_invalidPassword_unsuccessfulFlow() throws Exception {
        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(username,
            "wrongpassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }



}
