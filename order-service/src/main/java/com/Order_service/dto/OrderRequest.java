package com.Order_service.dto;

import com.Order_service.entity.Address;
import com.Order_service.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private List<Product> products;
    private Address address;
}
