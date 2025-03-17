package com.example.E_commerce.service.image;

import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Exceptions.ImageNotFoundException;
import com.example.E_commerce.dto.ImageDto;
import com.example.E_commerce.repository.ImageRepository;
import com.example.E_commerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(()-> new ImageNotFoundException("image with id: "+imageId+" Not Found!"));
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete , ()->{
                    throw new ImageNotFoundException("image with id: "+imageId+" Not Found!");
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> saveImagesDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "C:\\Users\\HP\\Downloads\\devices";
                String downloadUrl = buildDownloadUrl + image.getId();

                image.setDownloadUrl(downloadUrl);

                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();

                imageDto.setId(savedImage.getId());
                imageDto.setFileName(file.getOriginalFilename());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                saveImagesDto.add(imageDto);
            }catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return saveImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        if(image != null) {
            try {
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                imageRepository.save(image);
            }catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
