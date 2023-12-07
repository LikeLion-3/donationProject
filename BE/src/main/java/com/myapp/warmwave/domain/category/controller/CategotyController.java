package com.myapp.warmwave.domain.category.controller;

import com.myapp.warmwave.domain.category.dto.CategoryAllPostDto;
import com.myapp.warmwave.domain.category.dto.CategoryDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.service.CategoryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategotyController {
    private final CategoryService categoryService;

    @PostConstruct
    private void postAllCategory() {
        categoryService.createBasicCategories(new CategoryAllPostDto());
    }

    @PostMapping
    public ResponseEntity<CategoryDto> postCategory(@RequestBody CategoryDto dto) {
        Category category = categoryService.createCategory(dto);

        return ResponseEntity.ok(CategoryDto.builder()
                .name(category.getName())
                .build());
    }

    @GetMapping
    public Page<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PostMapping("/get")
    public ResponseEntity<List<CategoryDto>> getCategory(@RequestBody CategoryDto dto) {
        List<Category> categories = categoryService.getCategory(dto.getName());

        List<CategoryDto> categoryDtos = categories.stream()
                .map(category -> CategoryDto.builder()
                        .name(category.getName())
                        .build())
                .toList();

        return ResponseEntity.ok(categoryDtos);
    }

    @DeleteMapping
    public ResponseEntity deleteCategory(@RequestBody CategoryDto dto) {
        categoryService.deleteCategory(dto);

        return ResponseEntity.noContent().build();
    }
}
