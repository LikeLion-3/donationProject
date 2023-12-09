package com.myapp.warmwave.domain.category.repository;

import com.myapp.warmwave.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    void deleteByName(String name);
}
