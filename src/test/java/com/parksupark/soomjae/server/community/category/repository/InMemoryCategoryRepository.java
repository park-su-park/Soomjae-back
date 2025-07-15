package com.parksupark.soomjae.server.community.category.repository;

import com.parksupark.soomjae.server.community.category.entity.Category;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public InMemoryCategoryRepository() {
        Category root = Category.builder()
                .name("전체 카테고리")
                .parent(null)
                .hierarchy(0)
                .build();
        setIdByReflection(root, 1L);
        sequence.set(2); // ID 1L already used
        store.put(root.getId(), root);
    }


    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            Long newId = sequence.getAndIncrement();
            setIdByReflection(category, newId);
        }
        store.put(category.getId(), category);
        return category;
    }

    @Override
    public boolean existsByName(String name) {
        return store.values().stream()
                .anyMatch(category -> name.equals(category.getName()));
    }

    private void setIdByReflection(Category category, Long id) {
        try {
            Field idField = Category.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(category, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("ID 설정 실패", e);
        }
    }
}