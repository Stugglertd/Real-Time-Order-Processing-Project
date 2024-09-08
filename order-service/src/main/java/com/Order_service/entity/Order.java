package com.Order_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @NotNull(message = "Please Provide Customer Id")
    private Long CustomerId;
    @ElementCollection
    private List<Product> products;
    @Embedded
    private Address address;
    private String status;
}
