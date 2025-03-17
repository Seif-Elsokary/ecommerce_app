package com.example.E_commerce.service.Cart;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    void clearCart(long cartId);

    BigDecimal getTotalPrice(long cartId);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
