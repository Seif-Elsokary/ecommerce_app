package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.CartItem;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.repository.CartItemRepository;
import com.example.E_commerce.repository.CartRepository;
import com.example.E_commerce.service.Cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
    }



    @Test
    void getCart_ShouldThrowException_WhenCartNotFound() {
        // Mock عدم وجود السلة
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        // تنفيذ الاختبار مع التحقق من أنه يرمي استثناء
        assertThrows(ResourceNotFoundException.class, () -> cartService.getCart(1L));

        verify(cartRepository, times(1)).findById(1L);
    }



    @Test
    void initializeNewCart_ShouldReturnExistingCart_WhenCartAlreadyExists() {
        when(cartRepository.getCartByUserId(1L)).thenReturn(cart);

        Cart result = cartService.initializeNewCart(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartRepository, times(1)).getCartByUserId(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void initializeNewCart_ShouldCreateNewCart_WhenNoCartExists() {
        when(cartRepository.getCartByUserId(1L)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.initializeNewCart(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
