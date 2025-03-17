package com.example.E_commerce.dto;

import com.example.E_commerce.Entity.CartItem;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.service.Cart.CartItemService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;
    private Set<CartItemDto> item;
}
