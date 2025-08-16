package com.parksupark.soomjae.server.auth.oauth.service;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.auth.oauth.dto.CustomOAuth2User;
import com.parksupark.soomjae.server.auth.oauth.userinfo.OAuth2UserInfo;
import com.parksupark.soomjae.server.auth.oauth.userinfo.OAuth2UserInfoFactory;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider,
            oauth2User.getAttributes());
        
        // TODO: 이메일 인증 여부 체크

        Member member = findOrCreateMember(userInfo, provider);

        return new CustomOAuth2User(member, oauth2User.getAttributes());

    }

    private Member findOrCreateMember(OAuth2UserInfo userInfo, AuthProvider provider) {
        Optional<Member> existingMember = memberRepository.findByProviderAndProviderId(
            provider, userInfo.getProviderId());

        if (existingMember.isPresent()) {
            Member member = existingMember.get();

            log.info("기존 OAuth2 사용자 로그인: {}, provider: {}", userInfo.getEmail(), provider);

            // 이메일 주소가 변경되었을 수 있으니 업데이트
            if (!member.getEmail().equals(userInfo.getEmail())) {
                member.updateEmail(userInfo.getEmail());
                memberRepository.save(member);
                log.info("OAuth2 사용자 이메일 주소 업데이트: {} -> {}", member.getEmail(),
                    userInfo.getEmail());
            }

            return member;
        }

        Member newMember = Member.createOAuthMember(userInfo.getEmail(), provider,
            userInfo.getProviderId());

        Member savedMember = memberRepository.save(newMember);
        log.info("신규 OAuth2 사용자 생성: {}, provider: {}", userInfo.getEmail(), provider);

        return savedMember;
    }
}
