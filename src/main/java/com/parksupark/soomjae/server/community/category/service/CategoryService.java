package com.parksupark.soomjae.server.community.category.service;

import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NAME_DUPLICATE_ERROR;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.PARENT_CATEGORY_NOT_FOUND;
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
        validateDuplicateName(categoryRequestDto.getName());
        Category parent = resolveParentCategory(categoryRequestDto.getParentCategoryId());
        int hierarchy = calculateHierarchy(parent);

        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .parent(parent)
                .hierarchy(hierarchy)
                .build();

        return categoryRepository.save(category).getId();
    }

    private void validateDuplicateName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalStateException(CATEGORY_NAME_DUPLICATE_ERROR);
        }
    }

    private Category resolveParentCategory(String parentIdStr) {
        if (parentIdStr == null) {
            return categoryRepository.findById(1L)
                    .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
        }
        return categoryRepository.findById(Long.parseLong(parentIdStr))
                .orElseThrow(() -> new IllegalStateException(PARENT_CATEGORY_NOT_FOUND));
    }

    private int calculateHierarchy(Category parent) {
        return (parent.getId() == 1L) ? 1 : parent.getHierarchy() + 1;
    }

    public CategoryResponseDto readCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        return CategoryResponseDto.of(category);
    }

    public CategoryResponseDto readRootCategory() {
        Category category = categoryRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException(ROOT_CATEGORY_NOT_FOUND));
        return CategoryResponseDto.of(category);
    }

}
