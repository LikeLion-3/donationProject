package com.myapp.warmwave.domain.image.service;

import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

@Value("${image.upload.path}")
private String imageStorePath;

    private final ImageRepository imageRepository;

    public List<Image> uploadImages(Article article, List<MultipartFile> imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        File directory = ResourceUtils.getFile(imageStorePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                log.error("Failed to create directory: {}", imageStorePath);
            }
        }

        if (imageFiles == null) {
            return images;
        }

        for (MultipartFile imageFile : imageFiles) {
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            // 정확한 파일 시스템 경로를 얻기 위해 ResourceUtils.getFile() 사용
            File destFile = new File(directory, fileName);
            imageFile.transferTo(destFile);

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

    public List<Image> uploadImagesForCommunity(Community community, List<MultipartFile> imageFiles) {
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
            String fileName = UUID.randomUUID() + "." + fileExtension;
            // 작성자 이메일, createdAt, article PK + 중복 인덱스
            //
            try {
                imageFile.transferTo(Paths.get(imageStorePath).resolve(fileName));

                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/images/")
                        .path(fileName)
                        .toUriString();

                Image image = Image.builder()
                        .imgName(fileName)
                        .imgUrl(imageUrl)
                        .community(community)
                        .build();

                imageRepository.save(image);
                images.add(image);
            }
            catch(IOException e) {
                log.error("Failed to store images"+ String.valueOf(e));;
            }
        }
        return images;
    }

    public void deleteImagesByArticleId(Long articleId) {
        List<Image> imagesToDelete = imageRepository.findByArticleId(articleId);

        for (Image image : imagesToDelete) {
            String fileName = image.getImgName();
            File fileToDelete = new File(imageStorePath, fileName);

            // 파일 삭제
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }
        imageRepository.deleteAll(imagesToDelete);
    }
}