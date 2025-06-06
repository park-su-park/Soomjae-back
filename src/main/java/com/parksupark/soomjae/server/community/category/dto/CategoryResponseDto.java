package com.parksupark.soomjae.server.community.category.dto;

import com.parksupark.soomjae.server.community.category.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String name;
    private int hierarchy;

    private List<CategoryResponseDto> childs = new ArrayList<>();


    public static CategoryResponseDto of(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setHierarchy(category.getHierarchy());

        List<CategoryResponseDto> list = new ArrayList<>();
        if (category.getChilds() != null) {
            category.getChilds().forEach(c -> list.add(CategoryResponseDto.of(c)));
        }

        categoryResponseDto.setChilds(list);
        return categoryResponseDto;
    }
}
