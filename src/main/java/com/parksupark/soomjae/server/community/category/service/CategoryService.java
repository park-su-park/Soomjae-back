package com.parksupark.soomjae.server.community.category.service;

import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NAME_DUPLICATE_ERROR;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.PARENT_CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.ROOT_CATEGORY;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.ROOT_CATEGORY_NOT_FOUND;

import com.parksupark.soomjae.server.community.category.dto.CategoryRequestDto;
import com.parksupark.soomjae.server.community.category.dto.CategoryResponseDto;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long createCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.findByName(categoryRequestDto.getName()).isPresent()) {
            throw new IllegalStateException(CATEGORY_NAME_DUPLICATE_ERROR);
        }
        if (categoryRequestDto.getParentCategoryName() == null) {
            Category rootCategory = categoryRepository.findByName(ROOT_CATEGORY)
                    .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
            Category category = Category.builder()
                    .name(categoryRequestDto.getName())
                    .parent(rootCategory)
                    .hierarchy(1)
                    .build();
            return categoryRepository.save(category).getId();
        } else {
            Category parent = categoryRepository.findByName(
                            categoryRequestDto.getParentCategoryName())
                    .orElseThrow(() -> new IllegalStateException(PARENT_CATEGORY_NOT_FOUND));

            Category category = Category.builder()
                    .parent(parent)
                    .name(categoryRequestDto.getName())
                    .hierarchy(parent.getHierarchy() + 1)
                    .build();
            return categoryRepository.save(category).getId();
        }
    }

    public CategoryResponseDto readCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        return CategoryResponseDto.of(category);
    }

    public CategoryResponseDto readRootCategory() {
        Category category = categoryRepository.findByName(ROOT_CATEGORY)
                .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
        return CategoryResponseDto.of(category);
    }

}
