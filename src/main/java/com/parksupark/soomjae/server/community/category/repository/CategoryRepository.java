package com.parksupark.soomjae.server.community.category.repository;

import com.parksupark.soomjae.server.community.category.entity.Category;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    Category save(Category category);

    boolean existsByName(String name);
}
