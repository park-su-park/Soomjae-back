package com.parksupark.soomjae.server.community.category.repository;

import com.parksupark.soomjae.server.community.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryRepository extends JpaRepository<Category, Long>,
        CategoryRepository {

}
