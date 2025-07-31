package com.parksupark.soomjae.server.community.category.service;

import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.ROOT_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.parksupark.soomjae.server.community.category.dto.CategoryRequestDto;
import com.parksupark.soomjae.server.community.category.dto.CategoryResponseDto;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.category.repository.InMemoryCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryServiceTest {

    CategoryRepository categoryRepository;
    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = new InMemoryCategoryRepository();
        categoryService = new CategoryService(categoryRepository);

        categoryService.createCategory(new CategoryRequestDto("중복이름", null));
    }

    @Test
    @DisplayName("부모 카테고리 지정 x -> 카테고리의 부모가 전체카테고리로 지정되어 저장되어야한다.")
    void createCategory() {
        //given
        CategoryRequestDto requestDto = new CategoryRequestDto("category1", null);

        //when
        Long categoryId = categoryService.createCategory(requestDto);

        //then
        Category category = categoryRepository.findById(categoryId).get();
        Assertions.assertEquals("category1", category.getName());
        Assertions.assertEquals(ROOT_CATEGORY, category.getParent().getName());
    }

    @Test
    @DisplayName("부모 카테고리 지정 -> 해당 Id를 가진 카테고리를 부모 카테고리로 설정하여 저장한다.")
    void createCategoryWithParent() {
        //given
        Category category = Category.builder()
                .name("parentCategory")
                .build();
        Category parentCategory = categoryRepository.save(category);
        CategoryRequestDto requestDto = new CategoryRequestDto("childCategory",
                parentCategory.getId().toString());

        //when
        Long category1 = categoryService.createCategory(requestDto);
        Category parent = categoryRepository.findById(category1).get().getParent();

        //then
        Assertions.assertEquals(parentCategory, parent);
    }

    @Test
    @DisplayName("카테고리 ID로 카테고리 조회가 가능해야 한다.")
    void readCategory() {
        //given
        Category category = Category.builder()
                .name("category1")
                .build();
        Category save = categoryRepository.save(category);

        //when
        CategoryResponseDto categoryResponseDto = categoryService.readCategory(save.getId());

        //then
        Assertions.assertEquals(save.getName(), categoryResponseDto.getName());
    }

    @Test
    @DisplayName("루트 카테고리를 정상적으로 조회해야 한다.")
    void readRootCategory() {
        //when
        CategoryResponseDto categoryResponseDto = categoryService.readRootCategory();

        //then
        Assertions.assertEquals(ROOT_CATEGORY, categoryResponseDto.getName());
    }

    @Test
    @DisplayName("중복이름 카테고리 저장시 에러가 발생해야 한다.")
    void validDuplicateName() {
        // given
        CategoryRequestDto duplicateRequest = new CategoryRequestDto("중복이름", null);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            categoryService.createCategory(duplicateRequest);
        });
    }
}