package com.example.E_commerce.Entity;

import com.example.E_commerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")   // <-- تعديل مهم
    private Long orderId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_total_amount")
    private BigDecimal orderTotalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
