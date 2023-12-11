package com.myapp.warmwave.domain.image.repository;

import com.myapp.warmwave.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByArticleId(long articleId);
}
