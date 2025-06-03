package com.parksupark.soomjae.server.community.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.community.category.dto.CategoryRequestDto;
import com.parksupark.soomjae.server.community.category.dto.CategoryResponseDto;
import com.parksupark.soomjae.server.community.category.service.CategoryService;
import com.parksupark.soomjae.server.testconfig.TestSecurityConfig;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@Import({TestSecurityConfig.class})
@ActiveProfiles("test")
class CategoryControllerTestWithoutSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategory_shouldReturnId() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto("카테고리1", "1");

        when(categoryService.createCategory(any())).thenReturn(1L);

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("카테고리 단건 조회 테스트")
    void readCategory() throws Exception {
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "카테고리1", 1, null);
        when(categoryService.readCategory(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/categories")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("카테고리1"));
    }

    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void readAllCategories() throws Exception {
        CategoryResponseDto parentResponseDto = new CategoryResponseDto(1L, "전체 카테고리", 0,
                new ArrayList<>());
        CategoryResponseDto childResponseDto = new CategoryResponseDto(2L, "영어", 1,
                new ArrayList<>());
        parentResponseDto.getChilds().add(childResponseDto);

        when(categoryService.readRootCategory()).thenReturn(parentResponseDto);

        mockMvc.perform(get("/v1/categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("전체 카테고리"))
                .andExpect(jsonPath("$.childs").isArray())
                .andExpect(jsonPath("$.childs[0].id").value(2))
                .andExpect(jsonPath("$.childs[0].name").value("영어"))
                .andExpect(jsonPath("$.childs[0].childs").isArray())
                .andExpect(jsonPath("$.childs[0].childs.length()").value(0)).andDo(print());
    }
}