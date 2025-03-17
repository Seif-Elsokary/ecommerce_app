package com.example.E_commerce.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String description;
    private int quantity;
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "catregory_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(String name, String brand, String description, int quantity, BigDecimal price, Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }
}
