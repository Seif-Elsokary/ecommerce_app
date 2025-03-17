package com.example.E_commerce.dto;

import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
