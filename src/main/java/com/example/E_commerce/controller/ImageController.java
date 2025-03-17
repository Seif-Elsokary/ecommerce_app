package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.Exceptions.ImageNotFoundException;
import com.example.E_commerce.dto.ImageDto;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files , @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtoList = imageService.saveImages(files, productId);
            ApiResponse apiResponse = new ApiResponse("add Images successfully", imageDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ImageNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("add Images failed", e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @GetMapping("/image/download{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId , MultipartFile file) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1 , (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile file , @PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file ,imageId);
                ApiResponse apiResponse = new ApiResponse("update Image successfully", image);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed to update image!" , null));
        }catch (ImageNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("update Image failed", e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PutMapping("image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImageById(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                ApiResponse apiResponse = new ApiResponse("delete Image successfully", image);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed to delete image!" , null));
        }catch (ImageNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("update Image failed", e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
