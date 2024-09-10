package com.inventory_service.service;

import com.inventory_service.dto.Order;
import com.inventory_service.entity.Inventory;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface InventoryService {
    Inventory addProduct(Inventory inventory);
    void consumeOrder(ConsumerRecord<String,Order> record);
}
