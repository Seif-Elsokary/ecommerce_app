package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Image;
import com.example.E_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
