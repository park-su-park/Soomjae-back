package com.parksupark.soomjae.server.member.service;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.member.dto.CheckDuplicateEmailResponse;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.exception.DuplicateEmailException;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // nickname 필드에 대한 중복 체크는 추후에 자세한 nickname 초기화 정책이 나오면 구현
    @Transactional
    @Override
    public MemberResponse createMember(CreateMemberRequest request) {
        final String email = request.getEmail();

        validateEmailUniqueness(email);

        final String encodedPassword = passwordEncoder.encode(request.getPassword());
        final String nickname = request.getNickname();

        Member member = Member.create(email, encodedPassword, nickname);
        memberRepository.save(member);
        return createMemberResponse(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberResponse readMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new MemberNotFoundException(
                ErrorMessages.MEMBER_NOT_FOUND_EXCEPTION_MESSAGE));

        return createMemberResponse(member);
    }

    @Transactional
    @Override
    public MemberResponse updateEmail(Long id, String email) {

        Member member = findMemberById(id);

        validateEmailUniqueness(email);

        member.updateEmail(email);
        return createMemberResponse(member);
    }

    @Transactional
    @Override
    public MemberResponse updatePassword(Long id, String password) {
        Member member = findMemberById(id);

        String encodedPassword = passwordEncoder.encode(password);
        member.updatePassword(encodedPassword);

        return createMemberResponse(member);
    }

    // nickname 필드에 대한 중복 체크는 추후에 자세한 nickname 초기화 정책이 나오면 구현
    @Transactional
    @Override
    public MemberResponse updateNickname(Long id, String nickname) {
        Member member = findMemberById(id);

        member.updateNickname(nickname);

        return createMemberResponse(member);
    }

    @Transactional(readOnly = true)
    @Override
    public CheckDuplicateEmailResponse checkDuplicateEmail(String email) {
        return new CheckDuplicateEmailResponse(memberRepository.existsByEmail(email));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(
                        ErrorMessages.MEMBER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private MemberResponse createMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getNickname(),
            member.getRole(), member.getCreatedTime(), member.getModifiedTime());
    }


    // 추후 중복 검사가 필요한 필드가 늘어날 경우 확장해야함
    private void validateEmailUniqueness(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL_EXCEPTION_MESSAGE);
        }
    }

}
