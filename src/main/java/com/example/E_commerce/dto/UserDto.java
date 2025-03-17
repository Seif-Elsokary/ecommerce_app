package com.example.E_commerce.dto;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<OrderDto> orders;
    private CartDto cart;
}
