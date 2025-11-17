package com.parksupark.soomjae.server.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.parksupark.soomjae.server.email.repository.EmailVerificationRepository;
import com.parksupark.soomjae.server.email.repository.NoOpEmailVerificationRepository;
import com.parksupark.soomjae.server.member.dto.CheckDuplicateEmailResponse;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.exception.DuplicateEmailException;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import com.parksupark.soomjae.server.member.repository.JpaLikeInMemoryMemberRepository;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import com.parksupark.soomjae.server.member.util.MemberCreator;
import com.parksupark.soomjae.server.member.util.RandomNicknameCreator;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


class DefaultMemberServiceUnitTest {

    private final MemberRepository memberRepository = new JpaLikeInMemoryMemberRepository();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailVerificationRepository emailVerificationRepository =
        new NoOpEmailVerificationRepository();
    private final MemberCreator memberCreator = new MemberCreator(memberRepository,
        new RandomNicknameCreator(new SecureRandom()), null);
    private final String email = "test@example.com";
    private final String password = "test";
    private final String nickname = "test";

    private final MemberService memberService = new DefaultMemberService(passwordEncoder,
        memberRepository, emailVerificationRepository, memberCreator);

    @BeforeEach
    void setUp() {
        ((JpaLikeInMemoryMemberRepository) memberRepository).clear();
    }

    @Test
    void createMemberWithValidInformation_shouldCreateMemberAndEncodePassword() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(email, password);

        // when
        MemberResponse response = memberService.createMember(createMemberRequest);

        Member member = memberRepository.findById(response.getMemberId()).get();

        // then
        assertEquals(email, response.getEmail());
        assertEquals(nickname, response.getNickname());
        assertTrue(passwordEncoder.matches(password, member.getPassword()));
    }

    @Test
    void createMemberWithDuplicateEmail_shouldThrowDuplicateEmailException() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(email, password);

        saveMember();
        memberRepository.flush();

        // when + then
        assertThrows(DuplicateEmailException.class,
            () -> memberService.createMember(createMemberRequest));
    }

    @Test
    void readMemberWithValidId_shouldReturnMemberResponse() {
        // given
        Member member = saveMember();
        Long memberId = member.getId();

        // when
        MemberResponse memberResponse = memberService.readMember(memberId);

        // then
        assertEquals(member.getEmail(), memberResponse.getEmail());
        assertEquals(member.getId(), memberResponse.getMemberId());
        assertEquals(member.getNickname(), memberResponse.getNickname());
    }

    @Test
    void readMemberWithInvalidId_shouldThrowMemberNotFoundException() {
        // when + then
        assertThrows(MemberNotFoundException.class, () -> memberService.readMember(-1L));
    }

    @Test
    void updateEmailWithValidIdAndEmail_shouldUpdateEmail() {
        // given
        Member member = saveMember();
        Long memberId = member.getId();
        String newEmail = "new@example.com";

        // when
        MemberResponse memberResponse = memberService.updateEmail(memberId, newEmail);

        // then
        assertNotEquals(email, memberResponse.getEmail());
        assertEquals(newEmail, memberResponse.getEmail());
    }

    @Test
    void updateEmailWithInvalidId_shouldThrowMemberNotFoundException() {
        // when + then
        assertThrows(MemberNotFoundException.class, () -> memberService.updateEmail(-1L, "dummy"));
    }

    @Test
    void updateEmailWithDuplicateEmail_shouldDuplicateEmailException() {
        // given
        saveMember();
        Member member = Member.create("hellow@example.com", "test", "rlatnfla");
        memberRepository.save(member);
        memberRepository.flush();
        Long memberId = member.getId();

        // when + then
        assertThrows(DuplicateEmailException.class,
            () -> memberService.updateEmail(memberId, email));

    }

    @Test
    void updatePasswordWithValidIdAndPassword_shouldUpdatePassword() {
        // given
        Member member = saveMember();
        Long memberId = member.getId();
        String newPassword = "newpassword";

        // when
        memberService.updatePassword(memberId, newPassword);

        // then
        assertFalse(passwordEncoder.matches(password, member.getPassword()));
        assertTrue(passwordEncoder.matches(newPassword, member.getPassword()));
    }

    @Test
    void updatePasswordWithInvalidId_shouldThrowMemberNotFoundException() {
        assertThrows(MemberNotFoundException.class,
            () -> memberService.updatePassword(-1L, "dummy"));
    }

    @Test
    @DisplayName("이미 가입된 이메일을 중복 검사하면 true를 반환한다")
    void checkDuplicateEmailWithExistsEmail_shouldReturnTrue() {
        // given
        saveMember();

        // when
        CheckDuplicateEmailResponse response = memberService.checkDuplicateEmail(email);

        // then
        assertThat(response.isDuplicate()).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 검사시 중복되지 않았다면 false를 반환한다")
    void checkDuplicateEmailWithNonExistsEmail_shouldReturnFalse() {
        // when
        CheckDuplicateEmailResponse response = memberService.checkDuplicateEmail(email);

        // then
        assertThat(response.isDuplicate()).isFalse();
    }

    private Member saveMember() {
        Member member = memberRepository.save(
            Member.create(email, passwordEncoder.encode(password), nickname));
        memberRepository.flush();

        return member;
    }

}