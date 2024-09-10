package com.inventory_service.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Order{
    private Long orderId;
    private Long CustomerId;
    @ElementCollection
    private List<Product> products;
    @Embedded
    private Address address;
    private String status;
}
