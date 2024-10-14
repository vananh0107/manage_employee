package com.vandev.manage.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {
    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = spy(imageService);
    }

    @Test
    void saveImageWithTimestamp_ValidFile_Success() throws IOException {
        String originalFileName = "testImage.png";
        String expectedPath = "images/" + System.currentTimeMillis() + ".png";

        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getInputStream()).thenReturn(Files.newInputStream(Paths.get("src/test/resources/testImage.png")));

        String result = imageService.saveImageWithTimestamp(mockFile);

        Path savedFilePath = Paths.get("C:/Users/anhbv/code/manage_employee/src/main/resources/upload_images/images/" + result.substring(7));
        assertTrue(Files.exists(savedFilePath));

        Files.deleteIfExists(savedFilePath);
    }

    @Test
    void updateImage_ValidNewFile_Success() throws IOException {
        String existingFile = "images/existingImage.png";
        String newFileName = "newImage.png";

        when(mockFile.getOriginalFilename()).thenReturn(newFileName);
        when(mockFile.getInputStream()).thenReturn(Files.newInputStream(Paths.get("src/test/resources/testImage.png")));

        imageService.saveImageWithTimestamp(mockFile);
        String newImagePath = imageService.updateImage(existingFile, mockFile);

        Path savedNewFilePath = Paths.get("C:/Users/anhbv/code/manage_employee/src/main/resources/upload_images/images/" + newImagePath.substring(7));
        assertTrue(Files.exists(savedNewFilePath));

        Files.deleteIfExists(savedNewFilePath);
    }

    @Test
    void updateImage_NoFileProvided_ReturnsExistingFile() {
        String existingFile = "images/existingImage.png";

        when(mockFile.isEmpty()).thenReturn(true);
        when(mockFile.getOriginalFilename()).thenReturn(null);

        String result = imageService.updateImage(existingFile, mockFile);

        assertEquals(existingFile, result);
    }

    @Test
    void saveImageWithTimestamp_IOExceptionThrown_ReturnsFailureMessage() throws IOException {
        when(mockFile.getOriginalFilename()).thenReturn("invalidImage.txt");
        when(mockFile.getInputStream()).thenThrow(new IOException("Error during file upload"));

        String result = imageService.saveImageWithTimestamp(mockFile);

        assertEquals("Failed to upload image", result);
    }

    @Test
    void updateImage_WithNewImageAndIOException_ReturnsFailureMessage() throws IOException {
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getOriginalFilename()).thenReturn("newImage.jpg");
        when(imageFile.getInputStream()).thenThrow(new IOException("Error during file upload"));

        String existingFile = null;

        String result = imageService.updateImage(existingFile, imageFile);

        assertEquals("Failed to upload image", result);

        verify(imageFile, times(2)).getOriginalFilename();
        verify(imageFile, times(1)).getInputStream();
    }
}
