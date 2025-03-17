package com.example.E_commerce.service.image;

import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    List<ImageDto> saveImages(List<MultipartFile> files , Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
