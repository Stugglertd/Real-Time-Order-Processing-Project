package com.Order_service.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Product {
    private Long productId;
    private int quantity;
}
