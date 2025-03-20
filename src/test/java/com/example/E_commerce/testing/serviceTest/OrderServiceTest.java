package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Entity.OrderItem;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.dto.OrderDto;
import com.example.E_commerce.enums.OrderStatus;
import com.example.E_commerce.repository.OrderRepository;
import com.example.E_commerce.repository.ProductRepository;
import com.example.E_commerce.service.Cart.CartService;
import com.example.E_commerce.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDto orderDto;
    private Cart cart;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        // تجهيز بيانات الطلب
        order = new Order();
        order.setOrderId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderTotalAmount(BigDecimal.valueOf(1000));

        orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setOrderTotalAmount(BigDecimal.valueOf(1000));

        // تجهيز بيانات السلة والمنتجات
        product = new Product();
        product.setId(1L);
        product.setQuantity(10);

        cart = new Cart();
        cart.setId(1L);

        orderItem = new OrderItem(product, order, BigDecimal.valueOf(500), 2);
        order.setOrderItems(Set.of(orderItem));
    }

    @Test
    void testGetOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        OrderDto result = orderService.getOrder(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(BigDecimal.valueOf(1000), result.getOrderTotalAmount());
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getOrder(2L));

        assertEquals("Order Not Found!", exception.getMessage());
    }

    @Test
    void testPlaceOrder_Success() {
        when(cartService.getCartByUserId(1L)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doNothing().when(cartService).clearCart(cart.getId());
        Order placedOrder = orderService.placeOrder(1L);

        assertNotNull(placedOrder);
        assertEquals(OrderStatus.PENDING, placedOrder.getOrderStatus());
        assertEquals(LocalDateTime.now().getDayOfMonth(), placedOrder.getOrderDate().getDayOfMonth());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(1)).clearCart(cart.getId());
    }
}
