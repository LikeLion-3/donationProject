package com.myapp.warmwave.domain.category.service;

import com.myapp.warmwave.domain.category.dto.CategoryDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setup() {
        category = Category.builder()
                .id(1L)
                .name("카테고리1")
                .articleCategories(new ArrayList<>())
                .build();
    }

    @DisplayName("카테고리 생성 기능 확인")
    @Test
    void createCategory() {
        // given
        CategoryDto reqDto = new CategoryDto("카테고리1");

        when(categoryRepository.findByName(anyString())).thenReturn(null);
        when(categoryRepository.save(any())).thenReturn(category);

        // when
        Category foundCategory = categoryService.createCategory(reqDto);

        // then
        assertThat(foundCategory).isNotNull();
    }

    @DisplayName("카테고리 목록 조회 기능 확인")
    @Test
    void readAllCategory() {
        // given
        CategoryDto reqDto = new CategoryDto("카테고리1");

        when(categoryRepository.findByName(anyString())).thenReturn(null);
        when(categoryRepository.save(any())).thenReturn(category);

        Category savedCategory = categoryService.createCategory(reqDto);
        List<Category> categories = List.of(savedCategory);

        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        Page<Category> categoryPage = categoryService.getAllCategory();

        // then
        assertThat(categoryPage).hasSize(1);
    }

    @DisplayName("카테고리 삭제 기능 확인")
    @Test
    void deleteCategory() {
        // given
        CategoryDto reqDto = new CategoryDto("카테고리1");

        when(categoryRepository.findByName(anyString())).thenReturn(null);
        when(categoryRepository.save(any())).thenReturn(category);

        categoryService.createCategory(reqDto);

        // when
        categoryService.deleteCategory(reqDto);

        // then
        assertThat(categoryRepository.count()).isZero();
    }
}
