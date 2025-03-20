package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.ProductNotFoundException;
import com.example.E_commerce.dto.ProductDto;
import com.example.E_commerce.repository.CategoryRepository;
import com.example.E_commerce.repository.ProductRepository;
import com.example.E_commerce.request.AddProductRequest;
import com.example.E_commerce.request.UpdateProductRequest;
import com.example.E_commerce.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics");
        product = new Product("Laptop", "Dell", "High-end laptop", 10, BigDecimal.valueOf(1500.0), category);
        product.setId(1L);
    }

    @Test
    void addProduct_ShouldReturnSuccessfully() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Laptop");
        request.setBrand("Dell");
        request.setDescription("High-end laptop");
        request.setPrice(BigDecimal.valueOf(1500.0));
        request.setQuantity(10);
        request.setCategory(new Category("Electronics"));

        when(productRepository.existsByNameAndBrand(request.getName(), request.getBrand())).thenReturn(false);
        when(categoryRepository.findByName(request.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(request);

        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_ShouldThrowException_WhenProductAlreadyExist() {
        // إعداد الطلب
        AddProductRequest request = new AddProductRequest();
        request.setName("Laptop");
        request.setBrand("Dell");
        request.setCategory(new Category("Electronics")); // ✅ تجنب null

        when(productRepository.existsByNameAndBrand(request.getName(), request.getBrand())).thenReturn(true);

        assertThrows(AlreadyExistsFoundException.class, () -> productService.addProduct(request));

        verify(categoryRepository, never()).findByName(anyString());
    }


    @Test
    void getProductByCategory_ShouldReturnSuccessfully() {
        when(productRepository.findByCategoryName("Electronics")).thenReturn(List.of(product));

        List<Product> products = productService.getProductsByCategory("Electronics");

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void getProductByName_ShouldReturnSuccessfully() {
        when(productRepository.findByName("Laptop")).thenReturn(List.of(product));

        List<Product> products = productService.getProductsByName("Laptop");
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void updateProductById_ShouldReturnSuccessfully() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Gaming Laptop");
        request.setBrand("HP");
        request.setDescription("This is a laptop for gamers");
        request.setPrice(BigDecimal.valueOf(3200));
        request.setQuantity(15);
        request.setCategory(new Category("Electronics"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName(request.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(request, 1L);

        assertNotNull(updatedProduct);
        assertEquals("Gaming Laptop", updatedProduct.getName());
    }

    @Test
    void getAllProducts_ShouldReturnSuccessfully() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void deleteProductById_ShouldReturnSuccessfully(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProductById(1L);

        verify(productRepository, times(1)).delete(any(Product.class));
    }


    @Test
    void deleteProductById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(2L));
    }
}
