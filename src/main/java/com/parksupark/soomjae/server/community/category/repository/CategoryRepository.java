package com.parksupark.soomjae.server.community.category.repository;

import com.parksupark.soomjae.server.community.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findByName(String name);
}
