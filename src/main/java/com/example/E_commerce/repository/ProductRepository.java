package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByName(String name);

    List<Product> findByBrand(String brand);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByCategoryNameAndName(String category, String name);

    Long countProductsByCategoryNameAndBrand(String category, String brand);

    Long countProductsByCategoryName(String category);

    boolean existsByNameAndBrand(String name, String brand);
}
