package com.example.E_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;


    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
