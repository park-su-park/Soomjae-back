package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.member.entity.Member;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.dao.DataIntegrityViolationException;

public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Optional<Member> findById(Long memberId) {
        return store.values().stream()
            .filter(member -> member.getId().equals(memberId))
            .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
            .filter(member -> member.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public Member save(Member member) {
        validateEmailUniqueness(member);

        Long id = member.getId();

        if (id == null) {
            id = idSequence.getAndIncrement();
            setIdByReflection(member, id);
        }

        store.put(id, member);
        return member;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return store.values().stream()
            .anyMatch(member -> member.getNickname().equals(nickname));
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream()
            .anyMatch(member -> member.getEmail().equals(email));
    }

    @Override
    public void delete(Member member) {
        store.entrySet().removeIf(entry -> entry.getValue().equals(member));
    }

    @Override
    public void flush() {
        // nothing to do
    }

    @Override
    public Optional<Member> findByProviderAndProviderId(AuthProvider provider, String providerId) {
        return store.values().stream()
            .filter(member ->
                Objects.equals(provider, member.getProvider())
                    && Objects.equals(providerId, member.getProviderId()))
            .findFirst();
    }

    @Override
    public boolean existsByEmailAndProvider(String email, AuthProvider provider) {
        return store.values().stream()
            .anyMatch(
                member -> member.getEmail().equals(email) && member.getProvider().equals(provider));
    }

    public void clear() {
        store.clear();
    }

    private void setIdByReflection(Member member, Long id) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Like id 설정 실패", e);
        }
    }

    private void validateEmailUniqueness(Member member) {
        boolean isDuplicate = store.values().stream()
            .anyMatch(existing ->
                existing.getEmail().equals(member.getEmail())
            );

        if (isDuplicate) {
            throw new DataIntegrityViolationException(
                "Duplicate entry '" + member.getEmail() + "' for key 'email'");
        }
    }

    @Override
    public boolean exitsById(Long id) {
        return false;
    }
}
