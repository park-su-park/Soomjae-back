package com.parksupark.soomjae.server.community.category.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.community.category.dto.CategoryRequestDto;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.category.service.CategoryService;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test2")
class CategoryIntergrationTest {

    private static String token;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void setUp(@Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper,
            @Autowired MemberRepository memberRepository,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired CategoryRepository categoryRepository) throws Exception {

        Category category = new Category(null, "전체 카테고리", null, null, 0);
        categoryRepository.save(category);

        String username = "testuser";
        String password = "testpssword";
        String nickname = "testnickname";

        memberRepository.save(Member.create(username, passwordEncoder.encode(password), nickname));

        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(username,
                password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        token = jsonNode.get("accessToken").asText();
    }

    @Test
    @DisplayName("JWT 필터를 통과하여 카테고리 생성 성공")
    void createCategory_withJwt() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto("카테고리1", "1");

        mockMvc.perform(post("/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("카테고리 단건 조회 테스트")
    void readCategory() throws Exception {
        mockMvc.perform(get("/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("전체 카테고리"));
    }

    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void readAllCategories() throws Exception {
        CategoryRequestDto responseDto = new CategoryRequestDto("영어", null);
        categoryService.createCategory(responseDto);

        mockMvc.perform(get("/v1/categories/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("전체 카테고리"))
                .andExpect(jsonPath("$.childs").isArray())
                .andExpect(jsonPath("$.childs[0].id").value(2))
                .andExpect(jsonPath("$.childs[0].name").value("영어"))
                .andExpect(jsonPath("$.childs[0].childs").isArray())
                .andExpect(jsonPath("$.childs[0].childs.length()").value(0))
                .andDo(print());
    }
}