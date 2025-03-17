package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.ProductNotFoundException;
import com.example.E_commerce.dto.ProductDto;
import com.example.E_commerce.request.AddProductRequest;
import com.example.E_commerce.request.UpdateProductRequest;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.image.IImageService;
import com.example.E_commerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {


    private final ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product product1 = productService.addProduct(product);
            ProductDto productDto = productService.convertToDoList(product1);
            ApiResponse apiResponse = new ApiResponse("add Product Successfully!" , productDto);
            return ResponseEntity.ok(apiResponse);
        }catch (AlreadyExistsFoundException p){
            ApiResponse apiResponse = new ApiResponse("add Product Failed!" , p.getMessage());
            return ResponseEntity.status(CONFLICT).body(apiResponse);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest product ,@PathVariable Long productId) {
        try {
            Product product1 = productService.updateProduct(product, productId);
            ProductDto productDto = productService.convertToDoList(product1);
            ApiResponse apiResponse = new ApiResponse("update Product Successfully!" , productDto);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("update Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product1 = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDoList(product1);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDto);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        try {
            Product product1 = productService.getProductById(productId);
            if(product1 != null){
                ApiResponse apiResponse = new ApiResponse("delete Product Successfully!" , productId);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("delete Product Failed!" , productId));
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("delete Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> productList = productService.getAllProducts();
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get All Products Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get All Products Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{category}/product")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> productList = productService.getProductsByCategory(category);
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{name}/product")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> productList = productService.getProductsByName(name);
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{brand}/product")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brand) {
        try {
            List<Product> productList = productService.getProductByBrand(brand);
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> productList = productService.getProductsByBrandAndName(brand, name);
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndName(@RequestParam String category, @RequestParam String name) {
        try {
            List<Product> productList = productService.getProductsByCategoryAndName(category, name);
            List<ProductDto> productDtoList = productService.getProductDto(productList);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , productDtoList);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/count/by/category-and-brand")
    public ResponseEntity<ApiResponse> countProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            var product = productService.countProductsByCategoryAndBrand(category, brand);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , product);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/count/by/category/{category}")
    public ResponseEntity<ApiResponse> countProductsByCategoryAndBrand(@PathVariable String category) {
        try {
            var product = productService.countProductsByCategory(category);
            ApiResponse apiResponse = new ApiResponse("get Product Successfully!" , product);
            return ResponseEntity.ok(apiResponse);
        }catch (ProductNotFoundException p){
            ApiResponse apiResponse = new ApiResponse("get Product Failed!" , p.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }




}
