package com.parksupark.soomjae.server.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.jwt.access.AccessTokenGenerator;
import com.parksupark.soomjae.server.member.dto.*;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.exception.DuplicateEmailException;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import com.parksupark.soomjae.server.member.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccessTokenGenerator accessTokenGenerator;


    // === API 엔드포인트 상수
    private static final String CREATE_MEMBER_URI = "/v1/members/create-member";
    private static final String GET_MY_INFO_URI = "/v1/members/me";
    private static final String GET_MEMBER_INFO_URI = "/v1/members/{memberId}";
    private static final String PATCH_MEMBER_EMAIL_URI = "/v1/members/me/update-email";
    private static final String PATCH_MEMBER_NICKNAME_URI = "/v1/members/me/update-nickname";
    private static final String CHECK_DUPLICATE_EMAIL_URI = "/v1/members/check-duplicate-email";

    // === HTTP 헤더 상수
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // === 테스트 데이터
    private final String email = "test@example.com";
    private final String rawPassword = "test";
    private final String nickname = "testnickname";

    @Test
    @DisplayName("Member를 생성 할 수 있다")
    void createMember() throws Exception {
        // given
        CreateMemberRequest request = new CreateMemberRequest(email, rawPassword);

        // when + then
        mockMvc.perform(post(CREATE_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.nickname").value(nickname));
    }

    @Test
    @DisplayName("중복된 email로 Member 생성 요청시 400반환")
    void createMemberWithDuplicateEmail_shouldReturnBadRequest() throws Exception {
        // given
        CreateMemberRequest request = new CreateMemberRequest(email, rawPassword);

        memberService.createMember(request);

        // when + then
        mockMvc.perform(post(CREATE_MEMBER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }


    /**
     * [주의] 멀티스레드 동시성 테스트 - Transaction 격리 문제
     *
     * <p>작업스레드에서 생성된 Member 데이터가 실제 DB에 커밋되어
     * 다른 테스트에 영향을 미칠 수 있음
     *
     * <p><b>원인:</b> Spring의 ThreadLocal 기반 Transaction 격리<br>
     * - 작업스레드 → 메인 Transaction 접근 불가 → 새로운 Transaction 생성 → 즉시 커밋
     *
     * <p><b>TODO:</b> 고유한 테스트 데이터 사용하거나 별도 클래스로 분리 필요
     */
    @Test
    @DisplayName("중복된 email로 Member 생성 요청이 동시에 들어왔을때 둘중 하나는 DataIntegrityViolationException 던짐")
    void concurrentCreateMemberWithDuplicateEmail_shouldReturnBadRequest()
        throws InterruptedException {
        // given
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    // 이 데이터가 롤백되지 않고 db에 계속 남게됨
                    CreateMemberRequest request = new CreateMemberRequest("concurrent@example",
                        "password" + index);

                    MvcResult result = mockMvc.perform(post(CREATE_MEMBER_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                        .andReturn();

                    int status = result.getResponse().getStatus();
                    System.out.printf("Thread-%d: Status = %d%n", index, status);

                    if (result.getResponse().getStatus() == 200) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }

                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        assertEquals(1, successCount.get());
        assertEquals(1, failCount.get());
    }

    @Test
    @DisplayName("getMyInfo api 정상 동작")
    void getMyInfoWithValidAuthentication_shouldReturnMemberResponse() throws Exception {
        // given
        Member member = saveMember();
        String jwt = createJwt(member);

        // when + then
        mockMvc.perform(get(GET_MY_INFO_URI)
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + jwt)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberId").value(member.getId()))
            .andExpect(jsonPath("$.email").value(member.getEmail()))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()));
    }

    @Test
    @DisplayName("id로 사용자 조회")
    void getMemberInfoWithValidMemberId() throws Exception {
        // given
        Member member = saveMember();

        // when + then
        mockMvc.perform(get(GET_MEMBER_INFO_URI, member.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberId").value(member.getId()))
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.nickname").value(nickname));
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 id로 조회시 400")
    void getMemberInfoWithInvalidMemberId_shouldReturnBadRequest() throws Exception {
        // when + then
        mockMvc.perform(get(GET_MEMBER_INFO_URI, -1L)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> {
                assertThat(result.getResolvedException()).isInstanceOf(
                    MemberNotFoundException.class);
            });
    }

    @Test
    @DisplayName("patchMemberEmail 정상 동작")
    void updateMemberEmail_shouldReturnMemberResponse() throws Exception {
        // given
        Member member = saveMember();
        String jwt = createJwt(member);

        // 요청 바디 생성
        final String newEmail = "newemail@example.com";
        PatchEmailRequest patchEmailRequest = new PatchEmailRequest(newEmail);

        // when + then
        mockMvc.perform(patch(PATCH_MEMBER_EMAIL_URI)
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchEmailRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    @DisplayName("이미 존재하는 email 로 변경 시도시 400 반환")
    void updateMemberEmailWithDuplicateEmail_shouldReturnBadRequest() throws Exception {
        // given
        Member member = saveMember();
        String jwt = createJwt(member);

        // 이미 존재할 Member 생성
        final String duplicateEmail = "duplicate@example.com";
        CreateMemberRequest request = new CreateMemberRequest(duplicateEmail, "test");
        memberService.createMember(request);

        // 요청 바디 생성
        PatchEmailRequest patchEmailRequest = new PatchEmailRequest(duplicateEmail);

        // when + then
        mockMvc.perform(patch(PATCH_MEMBER_EMAIL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + jwt)
                .content(objectMapper.writeValueAsString(patchEmailRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> {
                assertThat(result.getResolvedException()).isInstanceOf(
                    DuplicateEmailException.class);
            });
    }

    @Test
    @DisplayName("patchMemberNickname 정상 동작")
    void patchMemberNickname_shouldReturnMemberResponse() throws Exception {
        // given
        Member member = saveMember();
        String jwt = createJwt(member);

        // 요청 바디 생성
        final String newNickname = "newnickname";
        PatchNicknameRequest patchNicknameRequest = new PatchNicknameRequest(newNickname);

        // when + then
        mockMvc.perform(patch(PATCH_MEMBER_NICKNAME_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + jwt)
                .content(objectMapper.writeValueAsString(patchNicknameRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value(newNickname));
    }

    @Test
    @DisplayName("중복된 이메일로 isDuplicateEmail 호출시 true 반환")
    void isDuplicateEmailWithExistsEmail_shouldReturnTrue() throws Exception {
        // given
        saveMember();

        // 요청 바디 생성
        CheckDuplicateEmailRequest request = new CheckDuplicateEmailRequest(email);

        // when + then
        mockMvc.perform(post(CHECK_DUPLICATE_EMAIL_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicate").value(true));
    }

    @Test
    @DisplayName("중복되지 않은 이메일로 isDuplicateEmail 호출시 false 반환")
    void isDuplicateEmailWithNonExistsEmail_shouldReturnFalse() throws Exception {
        // given
        saveMember();

        // 요청 바디 생성
        CheckDuplicateEmailRequest request = new CheckDuplicateEmailRequest("newEmail@example.com");

        // when + then
        mockMvc.perform(post(CHECK_DUPLICATE_EMAIL_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicate").value(false));
    }

    private String createJwt(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", member.getRole().getKey());
        return accessTokenGenerator.generate(member.getEmail(), claims);
    }

    private Member saveMember() {
        CreateMemberRequest request = new CreateMemberRequest(email, rawPassword);
        MemberResponse response = memberService.createMember(request);
        Long memberId = response.getMemberId();
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        return memberOpt.get();
    }
}
