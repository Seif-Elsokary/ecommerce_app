package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.ResourceNotFoundException;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.Cart.CartService;
import com.example.E_commerce.service.Cart.ICartItemService;
import com.example.E_commerce.service.User.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                     @RequestParam int quantity) {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable long cartId,
                                                          @PathVariable Long itemId) {
        try {
            cartService.getCart(cartId);  // يتحقق من وجود السلة
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item removed successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{cartId}/item/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @RequestParam Long productId,
                                                          @RequestParam int quantity) {
        try {
            cartService.getCart(cartId);  // يتحقق من وجود السلة
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item updated successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
