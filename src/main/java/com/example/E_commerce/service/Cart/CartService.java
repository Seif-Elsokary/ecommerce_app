package com.example.E_commerce.service.Cart;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.repository.CartItemRepository;
import com.example.E_commerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        BigDecimal totalAmount = cart.getItem().stream()
                .map(item -> item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);  // تحديث المجموع
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.getCartByUserId(userId);
    }

    @Override
    @Transactional
    public void clearCart(long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getItem().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);  // تحديث السلة بعد المسح
    }

    @Override
    public BigDecimal getTotalPrice(long cartId) {
        Cart cart = getCart(cartId);
        return cart.getItem().stream()
                .map(item -> item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
}
