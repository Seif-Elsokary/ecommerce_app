package com.example.E_commerce.service.product;

import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.dto.ProductDto;
import com.example.E_commerce.request.AddProductRequest;
import com.example.E_commerce.request.UpdateProductRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest addProduct);
    Product updateProduct(UpdateProductRequest updateProduct , Long id);
    Product getProductById(Long productId);
    void deleteProductById(Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByName(String name);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByCategoryAndName(String category, String name);
    Long countProductsByCategoryAndBrand(String category, String brand);
    Long countProductsByCategory(String category);

    List<ProductDto> getProductDto(List<Product> product);

    ProductDto convertToDoList(Product product);
}
