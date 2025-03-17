package com.example.E_commerce.dto;

import com.example.E_commerce.Entity.OrderItem;
import com.example.E_commerce.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal orderTotalAmount;
    private OrderStatus orderStatus;
    private Set<OrderItemDto> orderItems;
}
