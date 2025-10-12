package com.parksupark.soomjae.server.member.repository;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.member.Role;
import com.parksupark.soomjae.server.member.dto.MemberBasicInfo;
import com.parksupark.soomjae.server.member.entity.Member;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.dao.DataIntegrityViolationException;

public class JpaLikeInMemoryMemberRepository implements MemberRepository {

    // 실제 데이터 저장소 (DB)
    private final Map<Long, Member> persistentStore = new HashMap<>();

    // 영속성 컨텍스트 (EntityManager)
    private final Map<Long, Member> persistenceContext = new HashMap<>();

    // 엔티티 스냅샷 (더티 체크용)
    private final Map<Long, Member> entitySnapshots = new HashMap<>();

    // Id 시퀀스
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Optional<Member> findById(Long memberId) {

        // 1. 영속성 컨텍스트에서 조회 (1차 캐시)
        if (persistenceContext.containsKey(memberId)) {
            return Optional.of(persistenceContext.get(memberId));
        }

        // 2. 영속성 컨텍스트에 없으면 DB에서 조회
        Member storedMember = persistentStore.get(memberId);
        if (storedMember != null) {
            // 3. 조회한 엔티티를 영속성 컨텍스트에 관리
            Member managedMember = deepCopy(storedMember);
            persistenceContext.put(memberId, managedMember);

            // 4. 스냅샷 저장 (더티 체킹용)
            entitySnapshots.put(memberId, deepCopy(storedMember));

            // 영속성 컨텍스트에서 관리하는 객체를 반환
            return Optional.of(managedMember);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        // 1. 영속성 컨텍스트에서 조회 (1차 캐시)
        Optional<Member> contextResult = persistenceContext.values().stream()
            .filter(member -> email.equals(member.getEmail()))
            .findFirst();

        if (contextResult.isPresent()) {
            return contextResult;
        }

        // 2. DB에서 찾기
        Optional<Member> storeResult = persistentStore.values().stream()
            .filter(member -> email.equals(member.getEmail()))
            .findFirst();

        if (storeResult.isPresent()) {
            Member storedMember = storeResult.get();
            Member managedMember = deepCopy(storedMember);

            // 3. 1차 캐시에 저장
            persistenceContext.put(storedMember.getId(), managedMember);

            // 4. 스냅샷에 저장 (더티 체킹용)
            entitySnapshots.put(storedMember.getId(), deepCopy(storedMember));
            // 영속성 컨텍스트에서 관리하는 객체를 반환
            return Optional.of(managedMember);
        }

        return Optional.empty();
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null) {
            // 새로운 엔티티 insert
            long newId = idSequence.getAndIncrement();

            // reflection 을 사용하여 id 설정
            setIdByReflection(member, newId);

            // 영속성 컨텍스트에 관리
            persistenceContext.put(newId, member);
            entitySnapshots.put(newId, deepCopy(member));

            return member;
        } else {
            // 기존 엔티티 업데이트
            persistenceContext.put(member.getId(), member);

            // 스냅샷이 없다면 생성 (detached -> managed) 안전장치 개념
            if (!entitySnapshots.containsKey(member.getId())) {
                entitySnapshots.put(member.getId(), deepCopy(member));
            }

            return member;
        }
    }

    @Override
    public void flush() {
        // 1. 더티 체킹: 영속성 컨텍스트와 스냅샷 비교
        Set<Long> dirtyEntities = new HashSet<>();

        for (Map.Entry<Long, Member> entry : persistenceContext.entrySet()) {
            Long id = entry.getKey();
            Member currentEntity = entry.getValue();
            Member snapshot = entitySnapshots.get(id);

            if (snapshot == null || isDirty(currentEntity, snapshot)) {
                dirtyEntities.add(id);
            }
        }

        // 2. 변경된 엔티티들에 대해 제약 조건 검사
        validateConstraintsForPersistenceContext();

        // 3. DB에 반영 (실제로는 persistenceContext에 저장)
        for (Long dirtyId : dirtyEntities) {
            Member dirtyEntity = persistenceContext.get(dirtyId);

            // 현재는 commit 없이 flush 단계에서 commit 동작 하도록 구현
            // 사유: 너무 복잡함
            persistentStore.put(dirtyId, deepCopy(dirtyEntity));

            // 스냅샷 업데이트
            entitySnapshots.put(dirtyId, deepCopy(dirtyEntity));
        }
    }

    @Override
    public boolean existsByEmailAndProvider(String email, AuthProvider provider) {
        // 먼저 영속성 컨텍스트에서 확인
        boolean existsInContext = persistenceContext.values().stream()
            .anyMatch(
                member -> email.equals(member.getEmail()) && provider.equals(member.getProvider()));

        if (existsInContext) {
            return true;
        }

        return persistentStore.entrySet().stream()
            .filter(entry -> persistenceContext.containsKey(entry.getKey()))
            .anyMatch(entry -> email.equals(entry.getValue().getEmail()) && provider.equals(
                entry.getValue().getProvider()));
    }

    @Override
    public Optional<Member> findByProviderAndProviderId(AuthProvider provider, String providerId) {
        // 1. 영속성 컨텍스트에서 조회 (1차 캐시)
        Optional<Member> contextResult = persistenceContext.values().stream()
            .filter(member ->
                Objects.equals(provider, member.getProvider())
                    && Objects.equals(providerId, member.getProviderId()))
            .findFirst();

        if (contextResult.isPresent()) {
            return contextResult;
        }

        // 2. DB에서 찾기 (영속성 컨텍스트에 없는 엔티티만)
        Optional<Member> storeResult = persistentStore.entrySet().stream()
            .filter(
                entry -> !persistenceContext.containsKey(entry.getKey())) // 이미 영속성 컨텍스트에 있는 건 제외
            .map(Map.Entry::getValue)
            .filter(member ->
                Objects.equals(provider, member.getProvider())
                    && Objects.equals(providerId, member.getProviderId()))
            .findFirst();

        if (storeResult.isPresent()) {
            Member storedMember = storeResult.get();
            Member managedMember = deepCopy(storedMember);

            // 3. 1차 캐시에 저장
            persistenceContext.put(storedMember.getId(), managedMember);

            // 4. 스냅샷에 저장 (더티 체킹용)
            entitySnapshots.put(storedMember.getId(), deepCopy(storedMember));

            // 영속성 컨텍스트에서 관리하는 객체를 반환
            return Optional.of(managedMember);
        }

        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        // 먼저 영속성 컨텍스트에서 확인
        boolean existsInContext = persistenceContext.values().stream()
            .anyMatch(member -> email.equals(member.getEmail()));

        if (existsInContext) {
            return true;
        }

        // 영속성 컨텍스트에 없는 엔티티만 저장소에서 확인
        return persistentStore.entrySet().stream()
            .filter(entry -> !persistenceContext.containsKey(entry.getKey()))
            .anyMatch(entry -> email.equals(entry.getValue().getEmail()));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        // 영속성 컨텍스트와 저장소 모두 확인
        return persistenceContext.values().stream()
            .anyMatch(member -> nickname.equals(member.getNickname()))
            || persistentStore.values().stream()
                .anyMatch(member -> nickname.equals(member.getNickname()));
    }

    @Override
    public void delete(Member member) {
        Long id = member.getId();
        persistenceContext.remove(id);
        entitySnapshots.remove(id);
        persistentStore.remove(id);
    }

    @Override
    public Optional<MemberBasicInfo> findBasicInfoById(Long id) {
        return findById(id)
            .map(member -> new MemberBasicInfo() {
                @Override
                public Long getId() {
                    return member.getId();
                }

                @Override
                public String getEmail() {
                    return member.getEmail();
                }

                @Override
                public String getNickname() {
                    return member.getNickname();
                }

                @Override
                public Role getRole() {
                    return member.getRole();
                }

                @Override
                public Instant getCreatedTime() {
                    return member.getCreatedTime();
                }

                @Override
                public Instant getModifiedTime() {
                    return member.getModifiedTime();
                }
            });
    }

    private void validateConstraintsForPersistenceContext() {
        // 영속성 컨텍스트의 모든 엔티티에 대해 제약조건 검사

        validateEmailUniqueness(persistenceContext);

        // nickname 중복 검사 추후 구현
    }

    private void validateEmailUniqueness(Map<Long, Member> allEntities) {
        Map<String, Long> emailToIdMap = new HashMap<>();

        for (Member member : allEntities.values()) {
            String email = member.getEmail();
            if (email != null) {
                Long existingId = emailToIdMap.get(email);
                if (existingId != null) {
                    throw new DataIntegrityViolationException(
                        "Duplicate entry '" + email + "' for key 'email'");
                }
                emailToIdMap.put(email, member.getId());
            }
        }
    }

    // 더티 체킹
    private boolean isDirty(Member currentEntity, Member snapshot) {
        if (snapshot == null) {
            return true;
        }

        // 확장성 고려 더티 체킹 대상 필드 정의 -> 필드 추가시 더티 체크 필요 판단 후에 추가
        String[] fieldsToCheck = {"email", "nickname", "password"};

        // 리플렉션을 통한 더티 체킹
        try {
            for (String fieldName : fieldsToCheck) {
                Field field = Member.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                Object currentValue = field.get(currentEntity);
                Object snapshotValue = field.get(snapshot);

                if (!Objects.equals(currentValue, snapshotValue)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("dirty checking failed", e);
        }
    }

    private void setIdByReflection(Member member, long newId) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, newId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("failed to set member id", e);
        }
    }

    // 확장성 고려해야함
    private Member deepCopy(Member original) {
        try {
            Member copied = Member.create(original.getEmail(), original.getPassword(),
                original.getNickname());
            setIdByReflection(copied, original.getId());
            return copied;
        } catch (Exception e) {
            throw new RuntimeException("failed to copy member", e);
        }
    }

    // 테스트용 메서드들
    public void clear() {
        persistentStore.clear();
        persistenceContext.clear();
        entitySnapshots.clear();
        idSequence.set(1L);
    }

    public void clearPersistenceContext() {
        persistenceContext.clear();
        entitySnapshots.clear();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
