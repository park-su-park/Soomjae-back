package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.member.entity.Member;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
            .filter(member -> member.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public Member save(Member member) {
        Long id = member.getId();

        if (id == null) {
            id = idSequence.getAndIncrement();
            setIdByReflection(member, id);
        }

        store.put(id, member);
        return member;
    }

    @Override
    public void delete(Member member) {
        store.entrySet().removeIf(entry -> entry.getValue().equals(member));
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
}
