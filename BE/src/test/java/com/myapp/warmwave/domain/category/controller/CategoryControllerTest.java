package com.myapp.warmwave.domain.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.domain.category.dto.CategoryDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    protected Category newCategory() {
        return Category.builder()
                .id(1L)
                .name("카테고리1")
                .articleCategories(new ArrayList<>())
                .build();
    }

    @DisplayName("카테고리 생성")
    @Test
    void createCategory() throws Exception {
        // given
        CategoryDto reqDto = new CategoryDto("카테고리1");
        Category resCategory = newCategory();

        // when
        when(categoryService.createCategory(reqDto)).thenReturn(resCategory);

        // then
        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("category/카테고리 생성"));
    }

    @DisplayName("카테고리 목록 조회")
    @Test
    void readAllCategory() throws Exception {
        // given
        Page<Category> categoryPage = new PageImpl<>(List.of(newCategory()));

        // when
        when(categoryService.getAllCategory()).thenReturn(categoryPage);

        // then
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("category/카테고리 목록 조회"));
    }

    @DisplayName("카테고리 삭제")
    @Test
    void deleteCategory() throws Exception {
        // given
        CategoryDto reqDto = new CategoryDto("카테고리1");

        // when

        // then
        mockMvc.perform(delete("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("category/카테고리 삭제"));
    }
}
