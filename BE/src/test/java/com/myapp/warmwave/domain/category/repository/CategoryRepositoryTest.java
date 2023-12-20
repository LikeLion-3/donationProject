package com.myapp.warmwave.domain.category.repository;

import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.category.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category() {
        return Category.builder()
                .id(1L)
                .name("카테고리")
                .articleCategories(new ArrayList<>())
                .build();
    }

    // CREATE
    @DisplayName("카테고리 생성")
    @Test
    void createCategory() {
        // given
        Category category = category();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(savedCategory.getName()).isEqualTo(category.getName());
        assertThat(categoryRepository.count()).isEqualTo(1);
    }

    // READ
    @DisplayName("카테고리 전체 조회")
    @Test
    void readAllCategory() {
        // given
        categoryRepository.save(category());

        // when
        List<Category> categorieList = categoryRepository.findAll();

        // then
        assertThat(categorieList).hasSize(1);
    }

    // DELETE
    @DisplayName("카테고리 삭제")
    @Test
    void deleteCategory() {
        // given
        Category savedCategory = categoryRepository.save(category());

        // when
        categoryRepository.deleteByName(savedCategory.getName());

        Category foundCategory = categoryRepository.findByName(savedCategory.getName());

        // then
        assertThat(foundCategory).isNull();
    }
}
