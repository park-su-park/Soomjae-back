package com.parksupark.soomjae.server.auth.oauth.service;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.auth.oauth.dto.CustomOAuth2User;
import com.parksupark.soomjae.server.auth.oauth.exception.OAuth2AuthenticationProcessingException;
import com.parksupark.soomjae.server.auth.oauth.userinfo.OAuth2UserInfo;
import com.parksupark.soomjae.server.auth.oauth.userinfo.OAuth2UserInfoFactory;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import com.parksupark.soomjae.server.member.util.MemberCreator;
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
    private final MemberCreator memberCreator;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider,
            oauth2User.getAttributes());
        
        checkEmailVerification(userInfo);

        Member member = findOrCreateMember(userInfo, provider);

        return new CustomOAuth2User(member, oauth2User.getAttributes());

    }

    /**
     * OAuth2 사용자의 이메일 인증 상태를 검증
     * 
     * <p>보안상의 이유로 이메일이 인증되지 않은 사용자의 가입을 차단</p>
     *
     * @param userInfo OAuth2 Provider별 OAuth2UserInfo 구현체
     * @throws OAuth2AuthenticationProcessingException 이메일이 인증되지 않은 경우 발생
     */
    private void checkEmailVerification(OAuth2UserInfo userInfo) {
        if (!userInfo.isEmailVerified()) {
            throw new OAuth2AuthenticationProcessingException(
                ErrorMessages.OAUTH2_EMAIL_NOT_VERIFIED_MESSAGE);
        }
    }


    /**
     * OAuth2 제공자 정보를 기반으로 기존 회원을 조회하거나 신규 회원을 생성합니다.
     *
     * <p>OAuth2 로그인 시 다음과 같은 로직으로 회원을 처리합니다:</p>
     * <ul>
     *   <li><b>기존 회원 존재:</b> 이메일 정보 업데이트 후 반환</li>
     *   <li><b>신규 회원:</b> OAuth2 정보로 새 회원 생성 후 저장</li>
     * </ul>
     *
     * <p><b>이메일 업데이트 로직:</b><br>
     * OAuth2 제공자에서 사용자가 이메일을 변경했을 가능성이 있으므로,
     * 기존 회원의 이메일과 OAuth2에서 받은 이메일이 다르면 자동으로 업데이트합니다.</p>
     *
     * @param userInfo OAuth2 제공자별 사용자 정보 (GoogleOAuth2UserInfo, KakaoOAuth2UserInfo 등)
     * @param provider OAuth2 제공자 (GOOGLE, KAKAO, NAVER 등)
     * @return 조회되거나 새로 생성된 Member 엔티티
     */
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

        Member newMember = memberCreator.createOAuthMember(userInfo.getEmail(), provider,
            userInfo.getProviderId());

        Member member = memberRepository.save(newMember);
        log.info("신규 OAuth2 사용자 생성: {}, provider: {}", userInfo.getEmail(), provider);

        return member;
    }
}
