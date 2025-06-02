package com.parksupark.soomjae.server.community.like.repository;

import com.parksupark.soomjae.server.community.like.entity.Like;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryLikeRepository implements LikeRepository {

    private final Map<Long, Like> store = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Optional<Like> findByPostTypeAndPostIdAndMemberId(String postType, Long postId,
        Long memberId) {
        return store.values().stream()
            .filter(like -> like.getPostType().equals(postType)
                && like.getPostId().equals(postId)
                && like.getMember().getId().equals(memberId))
            .findFirst();
    }

    @Override
    public Long countByPostTypeAndPostId(String postType, Long postId) {
        return store.values().stream()
            .filter(like -> like.getPostType().equals(postType)
                && like.getPostId().equals(postId))
            .count();
    }

    @Override
    public boolean existsByPostTypeAndPostIdAndMemberId(String postType, Long postId,
        Long memberId) {
        return findByPostTypeAndPostIdAndMemberId(postType, postId, memberId).isPresent();
    }

    @Override
    public void delete(Like like) {
        store.entrySet().removeIf(entry -> entry.getValue().equals(like));
    }

    @Override
    public Like save(Like like) {
        Long id = like.getId();

        if (id == null) {
            id = idSequence.getAndIncrement();
            setIdByReflection(like, id);
        }

        store.put(id, like);
        return like;
    }

    public void clear() {
        store.clear();
    }

    private void setIdByReflection(Like like, Long id) {
        try {
            Field idField = Like.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(like, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Like id 설정 실패", e);
        }
    }
}
