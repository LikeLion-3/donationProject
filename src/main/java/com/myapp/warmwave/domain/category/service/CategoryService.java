package com.myapp.warmwave.domain.category.service;

import com.myapp.warmwave.domain.category.dto.CategoryAllPostDto;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> createAllCategory(CategoryAllPostDto categoryAllPostDto) {
        List<Category> categories = categoryAllPostDto.getNames()
                .stream()
                .map(name -> new Category(name))
                .collect(Collectors.toList());

        return categoryRepository.saveAll(categories);
    }


    public Optional<Category> getOne(long articleId) {
        return categoryRepository.findById(articleId);


    }
}
