package com.Order_service.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private String status;

    public OrderResponse(){

    }

    public OrderResponse(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
}
