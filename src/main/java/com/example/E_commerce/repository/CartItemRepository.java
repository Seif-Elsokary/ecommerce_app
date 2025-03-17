package com.example.E_commerce.repository;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteAllByCartId(Long cartId);
}
