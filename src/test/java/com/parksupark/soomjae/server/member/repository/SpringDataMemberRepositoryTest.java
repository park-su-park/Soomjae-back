package com.parksupark.soomjae.server.member.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.parksupark.soomjae.server.member.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback
class SpringDataMemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String email = "test@example.com";
    private final String password = passwordEncoder.encode("password");
    private final String nickname = "test";

    @Test
    void saveMember() {
        // given
        Member member = Member.create(email, password, nickname);

        // when
        Member saveMember = memberRepository.save(member);

        // then
        assertNotNull(saveMember);
        assertEquals(email, member.getEmail());
        assertEquals(nickname, member.getNickname());
    }

    @Test
    void saveMemberWithDuplicateEmail_shouldThrowsDataIntegrityViolationException() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        // when + then
        memberRepository.save(Member.create(email, password, "hello"));
        assertThatThrownBy(() -> {
            memberRepository.flush();
        })
            .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    void findMemberById() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        Long memberId = member.getId();

        // when
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // then
        assertTrue(foundMember.isPresent());
        assertEquals(email, foundMember.get().getEmail());
        assertEquals(nickname, foundMember.get().getNickname());
        assertEquals(password, foundMember.get().getPassword());
    }

    @Test
    void findMemberByIdWithInvalidId_shouldReturnEmpty() {
        // when
        Optional<Member> foundMember = memberRepository.findById(-1L);

        // then
        assertTrue(foundMember.isEmpty());
    }

    @Test
    void findMemberByEmail() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(member.getEmail());

        // then
        assertTrue(foundMember.isPresent());
        assertEquals(email, foundMember.get().getEmail());
        assertEquals(nickname, foundMember.get().getNickname());
        assertEquals(password, foundMember.get().getPassword());
    }

    @Test
    void findMemberByEmailWithInvalidEmail_shouldReturnEmpty() {
        // when
        Optional<Member> foundMember = memberRepository.findByEmail("empty");

        // then
        assertTrue(foundMember.isEmpty());
    }

    @Test
    void existByEmail() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        // when
        boolean exists = memberRepository.existsByEmail(member.getEmail());

        // then
        assertTrue(exists);
    }

    @Test
    void existsByEmailWithNonExistsEmail_shouldReturnFalse() {

        // when
        boolean exists = memberRepository.existsByEmail("nonexists@email.com");

        // then
        assertFalse(exists);
    }

    @Test
    void existsByNickname() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        // when
        boolean exists = memberRepository.existsByNickname(member.getNickname());

        // then
        assertTrue(exists);
    }

    @Test
    void existsByNicknameWithNonExistsNickname_shouldReturnFalse() {
        // when
        boolean exists = memberRepository.existsByNickname("nonexistsnickname");

        // then
        assertFalse(exists);
    }

    @Test
    void delete() {
        // given
        Member member = Member.create(email, password, nickname);
        memberRepository.save(member);

        // when
        memberRepository.delete(member);

        // then
        assertFalse(memberRepository.existsByEmail(member.getEmail()));
        assertFalse(memberRepository.existsByNickname(member.getNickname()));
        assertTrue(memberRepository.findById(member.getId()).isEmpty());
    }

}