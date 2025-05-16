package com.parksupark.soomjae.server.auth.username.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordUserDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UsernamePasswordUserDetailsService userDetailsService;

    private final String email = "test@example.com";

    @Test
    void loadUserByUsername_withValidEmail_returnsCorrectUserDetails() {
        final String password = "testPassword";

        // given
        Member member = Member.create(email, password);
        when(memberRepository.findByEmail(email))
            .thenReturn(Optional.of(member));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // then
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_withInvalidEmail_throwsUsernameNotFoundException() {
        // given
        when(memberRepository.findByEmail(email))
            .thenReturn(Optional.empty());

        // when + then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }
}