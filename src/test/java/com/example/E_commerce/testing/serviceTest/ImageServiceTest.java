package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.Exceptions.ImageNotFoundException;
import com.example.E_commerce.repository.ImageRepository;
import com.example.E_commerce.service.image.ImageService;
import com.example.E_commerce.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {


    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testGetImageById_ShouldReturnImage_WhenImageWithIdExists() {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);


        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        Image foundImage = imageService.getImageById(imageId);

        assertNotNull(foundImage);

        assertEquals(imageId, foundImage.getId());
    }

    @Test
    void testGetImageById_ShouldReturnImage_WhenImageWithIdDoesNotExist() {

        Long imageId = 2L;

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());
        assertThrows(ImageNotFoundException.class, () -> imageService.getImageById(imageId));
    }


    @Test
    void testDeleteImageById_ShouldDeleteImage_WhenImageWithIdExists() {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        imageService.deleteImageById(imageId);

        verify(imageRepository , times(1)).delete(image);
    }


    @Test
    void testDeleteImageById_ShouldDeleteImage_WhenImageWithIdDoesNotExist() {
        Long imageId = 2L;

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.deleteImageById(imageId));
    }





    @Test
    void testUpdateImage_ShouldUpdateImage_WhenImageExists() throws IOException, SQLException {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("updated.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{7, 8, 9});

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        imageService.updateImage(file, imageId);

        assertEquals("updated.jpg", image.getFileName());
        assertEquals("image/jpeg", image.getFileType());
        verify(imageRepository, times(1)).save(image);
    }


}
