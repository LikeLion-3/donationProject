package com.myapp.warmwave.domain.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    @Value("${image.upload.path}")
    private String imageStorePath;

    private final AmazonS3 amazonS3;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Image> uploadImages(Article article, List<MultipartFile> imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        if (imageFiles == null) {
            return images;
        }

        for (MultipartFile imageFile : imageFiles) {
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());
            metadata.setContentType(imageFile.getContentType());

            String key = "article/" + fileName;

            amazonS3.putObject(bucket, key, imageFile.getInputStream(), metadata);

            Image image = Image.builder()
                    .imgName(fileName)
                    .imgUrl(amazonS3.getUrl(bucket, key).toString())
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
            String key = "article/" + fileName;
            try {
                amazonS3.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }
        imageRepository.deleteAll(imagesToDelete);
    }

    public  void deleteImagesByUrls(List<String> urls) {
        String key = "";
        for(String url : urls) {
            if (url != null) {
                int lastSlashIndex = url.lastIndexOf('/');
                if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
                    key = url.substring(lastSlashIndex + 1);
                }
            }

            try {
                amazonS3.deleteObject(bucket, key);
            }  catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }
    }
}