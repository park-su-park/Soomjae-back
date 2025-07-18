package com.parksupark.soomjae.server.community.category.service;

import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NAME_DUPLICATE_ERROR;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.PARENT_CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.ROOT_CATEGORY;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.ROOT_CATEGORY_NOT_FOUND;

import com.parksupark.soomjae.server.community.category.dto.CategoryRequest;
import com.parksupark.soomjae.server.community.category.dto.CategoryResponse;
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
    public Long createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            throw new IllegalStateException(CATEGORY_NAME_DUPLICATE_ERROR);
        }
        if (categoryRequest.getParentCategoryId() == null) {
            Category rootCategory = categoryRepository.findByName(ROOT_CATEGORY)
                    .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
            Category category = Category.builder()
                    .name(categoryRequest.getName())
                    .parent(rootCategory)
                    .hierarchy(1)
                    .build();
            return categoryRepository.save(category).getId();
        } else {
            Category parent = categoryRepository.findById(
                            Long.parseLong(categoryRequest.getParentCategoryId()))
                    .orElseThrow(() -> new IllegalStateException(PARENT_CATEGORY_NOT_FOUND));

            Category category = Category.builder()
                    .parent(parent)
                    .name(categoryRequest.getName())
                    .hierarchy(parent.getHierarchy() + 1)
                    .build();
            return categoryRepository.save(category).getId();
        }
    }

    public CategoryResponse readCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        return CategoryResponse.of(category);
    }

    public CategoryResponse readRootCategory() {
        Category category = categoryRepository.findByName(ROOT_CATEGORY)
                .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
        return CategoryResponse.of(category);
    }

}
