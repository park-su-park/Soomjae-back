package com.parksupark.soomjae.server.community.category.service;

import com.parksupark.soomjae.server.community.category.dto.CategoryRequest;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        Category root = Category.builder()
                .name("전체 카테고리")
                .hierarchy(0)
                .parent(null)
                .build();
        categoryRepository.save(root);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @DisplayName("중복 이름 카테고리 생성시 예외가 발생해야 한다.")
    @Test
    void duplicate_category_test() {
        CategoryRequest requestDto1 = new CategoryRequest("이름1", null);
        categoryService.createCategory(requestDto1);
        CategoryRequest requestDto2 = new CategoryRequest("이름1", null);
        Assertions.assertThrows(IllegalStateException.class,
                () -> categoryService.createCategory(requestDto2),
                "중복된 이름의 카테고리를 생성하면 예외가 발생해야 합니다.");
    }

    @Transactional
    @DisplayName("부모 카테고리 없이 카테고리를 생성하면 루트 카테고리를 부모로 설정한다.")
    @Test
    void create_category_with_no_parent_should_use_root() {
        CategoryRequest requestDto = new CategoryRequest("운동", null);
        Long id = categoryService.createCategory(requestDto);

        Category savedCategory = categoryRepository.findById(id).orElseThrow();
        Assertions.assertEquals("운동", savedCategory.getName());
        Assertions.assertEquals(1, savedCategory.getHierarchy());
        Assertions.assertNotNull(savedCategory.getParent());
        Assertions.assertEquals("전체 카테고리", savedCategory.getParent().getName());
    }

    @DisplayName("부모 ID를 지정하면 해당 부모를 기준으로 하위 카테고리를 생성한다.")
    @Test
    void create_category_with_parent_should_use_parent() {
        Category parent = Category.builder()
                .name("상위 카테고리")
                .hierarchy(1)
                .parent(categoryRepository.findByName("전체 카테고리").orElseThrow())
                .build();
        categoryRepository.save(parent);

        CategoryRequest requestDto = new CategoryRequest("하위 카테고리",
                String.valueOf(parent.getId()));
        Long id = categoryService.createCategory(requestDto);

        Category savedCategory = categoryRepository.findById(id).orElseThrow();
        Assertions.assertEquals("하위 카테고리", savedCategory.getName());
        Assertions.assertEquals(2, savedCategory.getHierarchy());
        Assertions.assertEquals(parent.getId(), savedCategory.getParent().getId());
    }


}