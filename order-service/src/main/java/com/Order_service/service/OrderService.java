package com.Order_service.service;

import com.Order_service.dto.InventoryUpdate;
import com.Order_service.dto.OrderRequest;
import com.Order_service.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrderStatus(Long orderId);
    void updateOrderBasedOnInventory(InventoryUpdate inventoryUpdate);
    List<String> getDistinctCities();
}
