package com.example.E_commerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private Long item_id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
