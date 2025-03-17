package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
