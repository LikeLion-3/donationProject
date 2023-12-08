package com.myapp.warmwave.domain.category.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.domain.category.dto.CategoryAllPostDto;
import com.myapp.warmwave.domain.category.dto.CategoryDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.ALREADY_EXIST_CATEGORY;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_CATEGORY;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void createBasicCategories(CategoryAllPostDto categoryAllPostDto) {
        List<String> strCategories = parseCategoryStrToArray(categoryAllPostDto.getCategoryNames());

        List<Category> categories = strCategories.stream()
                .filter(strCategory -> categoryRepository.findByName(strCategory) == null)
                .map(strCategory -> Category.builder().name(strCategory).build())
                .toList();

        if (!categories.isEmpty()) {
            categoryRepository.saveAll(categories);
        }
    }

    public Category createCategory(CategoryDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Optional.ofNullable(categoryRepository.findByName(category.getName()))
                .ifPresent(existingCategory -> {
                    throw new CustomException(ALREADY_EXIST_CATEGORY);
                });

        return categoryRepository.save(category);
    }

    public Page<Category> getAllCategory() {
        List<Category> allCategories = categoryRepository.findAll();

        return new PageImpl<>(allCategories, PageRequest.of(1, allCategories.size()), allCategories.size());
    }

    private List<String> parseCategoryStrToArray(String input) {
        input = input.replaceAll("^\\[\"|\"\\]$", "");

        List<String> result = Arrays.stream(input.split(","))
                .map(word -> word.replaceAll("\"", "").trim())
                .collect(Collectors.toList());

        return result;
    }

    public List<Category> getCategory(String strCategories) {
        List<Category> categoryList = new ArrayList<>();

        for (String category : parseCategoryStrToArray(strCategories)) {
            categoryList.add(validateCategory(category));
        }

        return categoryList;
    }

    public void deleteCategory(CategoryDto dto) {
        categoryRepository.deleteByName(dto.getName());
    }

    private Category validateCategory(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByName(categoryName))
                .orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));
    }
}
