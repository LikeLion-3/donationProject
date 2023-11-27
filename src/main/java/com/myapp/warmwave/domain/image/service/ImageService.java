package com.myapp.warmwave.domain.image.service;

import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class ImageService {

@Value("${image.upload.path}")
private String imageStorePath = "/Users/wook/upload/article-images";

    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> upload(Article article, List<MultipartFile> imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        File directory = new File(imageStorePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                log.error("Failed to create directory: {}", imageStorePath);
            }
        }

        for (MultipartFile imageFile : imageFiles) {

            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;
            String filePath = imageStorePath + File.separator + fileName;

            imageFile.transferTo(new File(filePath));

            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();

            Image image = Image.builder()
                    .imgName(fileName)
                    .imgUrl(imageUrl)
                    .article(article)
                    .build();

            imageRepository.save(image);
            images.add(image);
        }
        return images;
    }
}
