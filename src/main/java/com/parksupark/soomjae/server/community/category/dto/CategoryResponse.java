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
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private int hierarchy;

    private List<CategoryResponse> childs = new ArrayList<>();

    public static CategoryResponse of(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setHierarchy(category.getHierarchy());
        List<CategoryResponse> list = new ArrayList<>();
        category.getChilds().forEach(c -> list.add(CategoryResponse.of(c)));
        categoryResponse.setChilds(list);
        categoryResponse.setHierarchy(categoryResponse.getHierarchy());
        return categoryResponse;
    }
}
