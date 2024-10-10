package com.vandev.manage.serviceImpl;

import com.vandev.manage.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public String saveImageWithTimestamp(MultipartFile imageFile){
        try {
            String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
            String customFileName = System.currentTimeMillis() + extension;
            String uploadDir = "C:/Users/anhbv/code/manage_employee/src/main/resources/upload_images/images/";
            Path filePath = Paths.get(uploadDir + customFileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "images/" + customFileName;
        }
        catch (IOException e){
            return "Failed to upload image";
        }
    }
    @Override
    public String updateImage(String existsFile,MultipartFile imageFile){
        try{
            if (!imageFile.isEmpty()) {
                if (existsFile != null && !existsFile.isEmpty()) {
                    Path oldImagePath = Paths.get(existsFile);
                    Path fullOldImagePath = Paths.get("C:/Users/anhbv/code/manage_employee/src/main/resources/upload_images/" + oldImagePath);
                    Files.deleteIfExists(fullOldImagePath);
                }
                String newImagePath = saveImageWithTimestamp(imageFile);
                return newImagePath;
            } else {
               return existsFile;
            }
        }
        catch (IOException e) {
            return "Failed to update image";
        }
    }
}
