package com.inventory_service.service;

import com.inventory_service.dto.Order;
import com.inventory_service.entity.Inventory;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface InventoryService {
    Inventory addProduct(Inventory inventory);
    void consumeOrder(ConsumerRecord<String,Order> record);
    List<String> getOutOfStockProducts();
}
