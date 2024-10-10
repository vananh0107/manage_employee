package com.vandev.manage.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveImageWithTimestamp(MultipartFile imageFile);
    String updateImage(String existsFile,MultipartFile imageFile);
}
