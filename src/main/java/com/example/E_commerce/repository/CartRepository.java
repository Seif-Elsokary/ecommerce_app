package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartByUserId(Long userId);

}
