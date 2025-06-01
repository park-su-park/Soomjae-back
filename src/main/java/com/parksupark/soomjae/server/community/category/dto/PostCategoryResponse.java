package com.parksupark.soomjae.server.community.category.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import lombok.Getter;

@Getter
public class PostCategoryResponse {

    Long id;
    String name;

    public PostCategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PostCategoryResponse of(Category category) {
        return new PostCategoryResponse(category.getId(), category.getName());
    }
}
