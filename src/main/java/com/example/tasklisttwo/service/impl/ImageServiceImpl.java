package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.model.exception.ImageDeleteException;
import com.example.tasklisttwo.model.exception.ImageDownloadException;
import com.example.tasklisttwo.model.exception.ImageUploadException;
import com.example.tasklisttwo.model.task.TaskImage;
import com.example.tasklisttwo.service.ImageService;
import com.example.tasklisttwo.service.props.MinioProperties;
import com.example.tasklisttwo.web.dto.task.CustomMultipartFile;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public ImageServiceImpl(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public String upload(TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + " " + e.getMessage());
        }
        MultipartFile multipartFile = image.getFile();
        if (multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name!");
        }
        String fileName = generateFileName(multipartFile);
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + " " + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @Override
    public List<TaskImage> getImages(List<String> fileNames) {
        List<TaskImage> images = new ArrayList<>();
        for (String filename : fileNames) {
            GetObjectArgs oArgs = GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(filename)
                    .build();
            try (InputStream inputStream = minioClient.getObject(oArgs)) {
                String extension = getExtension(filename);
                MultipartFile file = new CustomMultipartFile(
                        filename, filename, "image/" + extension, inputStream.readAllBytes()
                );
                TaskImage taskImage = new TaskImage();
                taskImage.setFile(file);
                images.add(taskImage);
            } catch (Exception e) {
                throw new ImageDownloadException("Image download failed!");
            }
        }
        return images;
    }

    @Override
    public void delete(String name) {
        RemoveObjectArgs rArgs = RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(name)
                .build();
        try {
            minioClient.removeObject(rArgs);
        } catch (Exception e) {
            throw new ImageDeleteException("Image delete failed!");
        }
    }

    @Override
    public void deleteAll(List<String> names) {
        for (String name : names) {
            RemoveObjectArgs rArgs = RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(name)
                    .build();
            try {
                minioClient.removeObject(rArgs);
            } catch (Exception e) {
                throw new ImageDeleteException("Image delete failed!");
            }
        }
    }

    private void createBucket()
            throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException,
            InternalException {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    private String getExtension(String extension) {
        return extension.substring(extension.lastIndexOf(".") + 1);
    }

    private void saveImage(InputStream inputStream, String fileName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(inputStream, inputStream.available(), -1)
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed!");
        }
    }

}
