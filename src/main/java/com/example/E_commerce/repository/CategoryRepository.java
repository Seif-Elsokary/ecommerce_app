package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    List<Category> findCategoryByName(String name);

    boolean existsByName(String name);
}
