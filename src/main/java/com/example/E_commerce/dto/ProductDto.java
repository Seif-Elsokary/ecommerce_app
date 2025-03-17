package com.example.E_commerce.dto;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Entity.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private Long id;

    private String name;
    private String brand;
    private String description;
    private int quantity;
    private BigDecimal price;
    private Category category;
    private List<ImageDto> images;
}
