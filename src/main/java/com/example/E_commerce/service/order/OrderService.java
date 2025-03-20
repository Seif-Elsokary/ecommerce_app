package com.example.E_commerce.service.order;

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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setOrderTotalAmount(calculateTotalAmount(orderItems));
        Order orderSaved = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return orderSaved;
    }


    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order , Cart cart) {
        return cart.getItem()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setQuantity(product.getQuantity() -  cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            product,
                            order,
                            cartItem.getUnitPrice(),
                            cartItem.getQuantity());
                }).toList();

    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(orderItem ->  orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToDto)
                .toList();
   }
    @Override
    public OrderDto convertToDto(Order order) {

        return modelMapper.map(order, OrderDto.class);
   }
}
