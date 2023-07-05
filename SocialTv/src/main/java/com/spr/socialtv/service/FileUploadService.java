package com.spr.socialtv.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public FileUploadService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        File convertedFile = convertMultiPartFileToFile(file);
        String fileName = generateFileName(file);
        String safeFileName = getSafeFileName(fileName);
        amazonS3.putObject(new PutObjectRequest(bucketName, safeFileName, convertedFile));
        convertedFile.delete();
        return safeFileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename();
    }

    private String getSafeFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }
}