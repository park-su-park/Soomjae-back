package com.parksupark.soomjae.server.community.category.repository;

import com.parksupark.soomjae.server.community.category.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category root = Category.builder()
                .name("전체 카테고리")
                .hierarchy(0)
                .parent(null)
                .build();
        categoryRepository.save(root);
    }

    @Test
    void 저장과_Id조회가_잘되어야_한다() {
        Category category1 = Category.builder()
                .name("카테고리1")
                .build();
        Category savedCategory = categoryRepository.save(category1);
        Category findedCategory = categoryRepository.findById(savedCategory.getId()).get();
        Assertions.assertEquals(savedCategory, findedCategory);
    }

    @Test
    void 이름으로_조회시_잘_조회되어야한다() {
        Category category = categoryRepository.findByName("전체 카테고리").get();
        Assertions.assertEquals("전체 카테고리", category.getName());
    }

}