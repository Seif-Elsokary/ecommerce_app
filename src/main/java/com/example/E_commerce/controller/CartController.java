package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.Cart.CartService;
import com.example.E_commerce.service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService CartService;
    private final com.example.E_commerce.service.Cart.CartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = CartService.getCart(cartId);
            ApiResponse apiResponse = new ApiResponse("get Cart success", cart);
            return ResponseEntity.ok(apiResponse);
        }catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse(e.getMessage(), null);
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }
    @DeleteMapping("/{cartId}/cart/delete")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable Long cartId) {
        try {
            Cart cart = CartService.getCart(cartId);
            if (cart != null) {
                cartService.clearCart(cartId);
                ApiResponse apiResponse = new ApiResponse("delete Cart success", null);
                return ResponseEntity.ok(apiResponse);
            }
            ApiResponse apiResponse = new ApiResponse("delete Cart failed", null);
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse(e.getMessage(), null);
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable long cartId){
        try {
            Cart cart = CartService.getCart(cartId);
            if (cart != null) {
                cartService.getTotalPrice(cartId);
                ApiResponse apiResponse = new ApiResponse("get totalPrice success", null);
                return ResponseEntity.ok(apiResponse);
            }
            ApiResponse apiResponse = new ApiResponse("get totalPrice failed", null);
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }catch (ResourceNotFoundException e){
            ApiResponse apiResponse = new ApiResponse(e.getMessage(), null);
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }
}
