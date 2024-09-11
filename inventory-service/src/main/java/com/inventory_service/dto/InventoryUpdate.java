package com.inventory_service.dto;

import lombok.Data;

@Data
public class InventoryUpdate {
    private Long orderId;
    private String status;
}
