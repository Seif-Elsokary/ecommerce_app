package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.CartItem;
import com.example.E_commerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}
