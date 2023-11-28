package com.myapp.warmwave.domain.category.controller;

import com.myapp.warmwave.domain.category.dto.CategoryAllPostDto;
import com.myapp.warmwave.domain.category.dto.CategoryDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategotyController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity postCategory(@RequestBody CategoryDto dto) {
        Category category = categoryService.createCategory(Category.builder()
                .name(dto.getName())
                .build());

        return ResponseEntity.ok(CategoryDto.builder()
                .name(category.getName())
                .build());
    }

    @PostMapping("/init")
    public ResponseEntity postAllCategory(@RequestBody @Valid CategoryAllPostDto categoryAllPostDto) {
        List<Category> categories = categoryService.createAllCategory(categoryAllPostDto);
        return new ResponseEntity(categories, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity getCategory(@PathVariable("categoryId") long categoryId) {
        Optional<Category> category = categoryService.getOne(categoryId);

        return ResponseEntity.ok(CategoryDto.builder()
                .name(category.get().getName())
                .build());
    }
}
