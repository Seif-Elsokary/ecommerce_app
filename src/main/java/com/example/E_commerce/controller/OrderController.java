package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.dto.OrderDto;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
        try {
            Order order1 = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order1);
            ApiResponse apiResponse = new ApiResponse("create order successfully!" , orderDto);
            return ResponseEntity.ok(apiResponse);
        }catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("create order failed!" , e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            if (order == null) {
                ApiResponse apiResponse = new ApiResponse("order Not Found!" , null);
                return ResponseEntity.status(NOT_FOUND).body(apiResponse);
            }
            return ResponseEntity.ok(new ApiResponse("get order successfully!" , order));
        }
        catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("get order failed!" , e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> order = orderService.getUserOrders(userId);
            if (order == null) {
                ApiResponse apiResponse = new ApiResponse("order Not Found!" , null);
                return ResponseEntity.status(NOT_FOUND).body(apiResponse);
            }
            return ResponseEntity.ok(new ApiResponse("get order successfully!" , order));
        }
        catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse("get order failed!" , e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }
}
