package com.example.E_commerce.service.Cart;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.CartItem;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.repository.CartItemRepository;
import com.example.E_commerce.repository.CartRepository;
import com.example.E_commerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Product product = productService.getProductById(productId);
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getItem()
                .stream()
                .filter(item -> item.getProduct().getId()
                            .equals(productId))
                .findFirst()
                .orElse(new CartItem());

        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItem()
                .stream()
                .filter(items -> items.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(items -> {
                    items.setQuantity(quantity);
                    items.setUnitPrice(items.getProduct().getPrice());
                    items.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItem()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItem()
                .stream()
                .filter(items -> items
                        .getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new ResolutionException("product not found!"));
    }

}
