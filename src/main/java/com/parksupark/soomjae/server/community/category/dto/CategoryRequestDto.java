package com.parksupark.soomjae.server.community.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    private String name;
    private String parentCategoryId;
}
