package com.parksupark.soomjae.server.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.member.controller.MemberController;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.service.MemberService;
import com.parksupark.soomjae.server.member.service.NoOpMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class Config {

        @Bean
        @Primary
        public MemberService memberService() {
            return new NoOpMemberService();
        }
    }

    // === 유효한 값들 ===
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "validPassword123";

    // === 유효하지 않은 값들 ===
    private static final String EMPTY_VALUE = "";
    private static final String WHITESPACE_VALUE = "   ";
    private static final String INVALID_EMAIL_FORMAT = "invalid-email-format";

    // === API 엔드포인트 ===
    private static final String POST_MEMBER_URI = "/v1/members/create-member";

    @Test
    @DisplayName("유효한 형식으로 postMember 호출시")
    void postMemberWithValidData_shouldReturnOk() throws Exception {
        // given
        CreateMemberRequest request = new CreateMemberRequest(VALID_EMAIL, VALID_PASSWORD);

        // when + then
        mockMvc.perform(post(POST_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 이메일 형식으로 postMember 호출시")
    void postMemberWithInvalidEmailFormat_shouldReturnBadRequest() throws Exception {
        // given
        CreateMemberRequest invalidEmailFormatRequest = new CreateMemberRequest(
            INVALID_EMAIL_FORMAT,
            VALID_PASSWORD);

        // when + then
        mockMvc.perform(post(POST_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailFormatRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공백이 포함된 필드값으로 postMember 호출시")
    void postMemberWithWhitespaceField_shouldReturnBadRequest() throws Exception {
        // given
        CreateMemberRequest blankSpaceRequest = new CreateMemberRequest(WHITESPACE_VALUE,
            WHITESPACE_VALUE);

        // when + then
        mockMvc.perform(post(POST_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blankSpaceRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("빈 필드값으로 postMember 호출시")
    void postMemberWithEmptyField_shouldReturnBadRequest() throws Exception {
        // given
        CreateMemberRequest request = new CreateMemberRequest(EMPTY_VALUE, EMPTY_VALUE);

        // when + then
        mockMvc.perform(post(POST_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
