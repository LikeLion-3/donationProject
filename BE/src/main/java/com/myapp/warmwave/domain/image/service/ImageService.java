package com.myapp.warmwave.domain.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final long maxFileAmount = 5;
    private static final long maxFileSizePerFile = 5 * 1024 * 1024;

    public List<Image> uploadImages(Article article, List<MultipartFile> imageFiles) throws IOException {
        validateImageFiles(imageFiles); // 예외처리 메서드

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

    private static void validateImageFiles(List<MultipartFile> imageFiles) {
        if (imageFiles.size() > maxFileAmount) {
          throw  new CustomException(IMAGE_AMOUNT_OVER);
        }

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile.getSize() > maxFileSizePerFile) {
               throw  new CustomException(IMAGE_SIZE_OVER);
            }
        }
    }

    public List<Image> uploadImagesForCommunity(Community community, List<MultipartFile> imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();
        System.out.println("imageService's images : " + images);
        if(imageFiles==null) {
            return images;
        }

        for (MultipartFile imageFile : imageFiles) {
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());
            metadata.setContentType(imageFile.getContentType());

            String key = "community/"+fileName;

            amazonS3.putObject(bucket, key, imageFile.getInputStream(), metadata);

            Image image = Image.builder()
                    .imgName(fileName)
                    .imgUrl(amazonS3.getUrl(bucket, key).toString())
                    .community(community)
                    .build();

            imageRepository.save(image);
            images.add(image);
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

    public void deleteImagesByCommunityId(Long communityId) {
        List<Image> imagesToDelete = imageRepository.findByCommunityId(communityId);
        if(imagesToDelete.isEmpty()){
            System.out.println("이미지 없음");
            throw new CustomException(CustomExceptionCode.NOT_FOUND_IMAGE);
        }

        for (Image image : imagesToDelete) {
            String fileName = image.getImgName();
            String key = "community/" + fileName;
            try {
                System.out.println("key : " + key);
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
        imageRepository.deleteAllByImgUrl(urls);
    }
}