package com.example.E_commerce.service.product;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.ProductNotFoundException;
import com.example.E_commerce.dto.ImageDto;
import com.example.E_commerce.dto.ProductDto;
import com.example.E_commerce.repository.CategoryRepository;
import com.example.E_commerce.repository.ImageRepository;
import com.example.E_commerce.repository.ProductRepository;
import com.example.E_commerce.request.AddProductRequest;
import com.example.E_commerce.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest addProduct) {

        if (productExists(addProduct.getName() , addProduct.getBrand())){
            throw new AlreadyExistsFoundException(addProduct.getName() + " " + addProduct.getBrand() + " already exists!, you may update this product instead!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(addProduct.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(addProduct.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        addProduct.setCategory(category);
        return productRepository.save(createProduct(addProduct , category));
    }

    private Product createProduct(AddProductRequest addProduct , Category category) {
        return new Product(
                addProduct.getName(),
                addProduct.getBrand(),
                addProduct.getDescription(),
                addProduct.getQuantity(),
                addProduct.getPrice(),
                category
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest updateProduct, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateProduct(updateProduct , existingProduct))
                .map(productRepository::save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    private Product updateProduct(UpdateProductRequest updateRequest , Product existingProduct){
        existingProduct.setName(updateRequest.getName());
        existingProduct.setDescription(updateRequest.getDescription());
        existingProduct.setPrice(updateRequest.getPrice());
        existingProduct.setBrand(updateRequest.getBrand());
        existingProduct.setPrice(updateRequest.getPrice());

        Category category = categoryRepository.findByName(updateRequest.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("product with id: "+productId+" Not Found!"));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete , ()->{
                    throw new ProductNotFoundException("product with id: "+productId+" Not Found!");
                });
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand , name);
    }

    @Override
    public List<Product> getProductsByCategoryAndName(String category, String name) {
        return productRepository.findByCategoryNameAndName(category , name);
    }


    @Override
    public Long countProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.countProductsByCategoryNameAndBrand(category , brand);
    }

    @Override
    public Long countProductsByCategory(String category) {
        return productRepository.countProductsByCategoryName(category);
    }

    private boolean productExists(String name , String brand ){
        return productRepository.existsByNameAndBrand(name , brand);
    }

    @Override
    public List<ProductDto> getProductDto(List<Product> product){
        return product.stream()
                .map(this :: convertToDoList)
                .toList();
    }

    @Override
    public ProductDto convertToDoList(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtoList = images.stream()
                .map(image -> modelMapper.map(image , ImageDto.class))
                .toList();

        productDto.setImages(imageDtoList);
        return productDto;

    }
}
