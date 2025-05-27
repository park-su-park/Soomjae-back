package com.parksupark.soomjae.server.member.service;

import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.dto.CreateMemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public CreateMemberResponse createMember(CreateMemberRequest request) {
        final String email = request.getEmail();
        final String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.create(email, encodedPassword);
        memberRepository.save(member);

        return new CreateMemberResponse(member.getId(), member.getEmail());
    }

}
