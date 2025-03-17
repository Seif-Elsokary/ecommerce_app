package com.example.E_commerce.service.order;

import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.dto.OrderDto;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
