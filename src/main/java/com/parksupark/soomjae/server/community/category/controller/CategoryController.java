package com.parksupark.soomjae.server.community.category.controller;

import com.parksupark.soomjae.server.community.category.dto.CategoryRequest;
import com.parksupark.soomjae.server.community.category.dto.CategoryResponse;
import com.parksupark.soomjae.server.community.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/v1/categories")
    public ResponseEntity<Long> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @GetMapping("/v1/categories")
    public ResponseEntity<CategoryResponse> readCategory(
            @RequestParam(name = "id", defaultValue = "0") Long id) {
        return ResponseEntity.ok(categoryService.readCategory(id));
    }

    @GetMapping("/v1/categories/all") //트리 구조에서 루트 노드를 조회 함으로써 전체 카테고리를 조회한다.
    public ResponseEntity<CategoryResponse> readAllCategories() {
        return ResponseEntity.ok(categoryService.readRootCategory());
    }

}
