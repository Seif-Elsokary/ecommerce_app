package com.example.E_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount =BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart"
               ,cascade = CascadeType.ALL
               ,orphanRemoval = true)
//    @JsonIgnore
    private Set<CartItem> item = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItem item) {
        this.item.add(item);
        item.setCart(this);
        updateTotalAmount();
    }


    public void removeItem(CartItem item) {
        this.item.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = item.stream()
                .map(items->{
                    BigDecimal unitPrice = items.getUnitPrice();
                    if (unitPrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(items.getQuantity()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}