package com.myapp.warmwave.domain.image.repository;

import com.myapp.warmwave.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByArticleId(long articleId);

    List<Image> findByCommunityId(long communityId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.imgUrl IN :imgUrls")
    void deleteAllByImgUrl(@Param("imgUrls") List<String> imgUrls);
}
