package com.inventory_service.service;

import com.inventory_service.dto.Order;
import com.inventory_service.dto.Product;
import com.inventory_service.entity.Inventory;
import com.inventory_service.repo.InventoryRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepo inventoryRepo;

    public InventoryServiceImpl(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    @Override
    public Inventory addProduct(Inventory inventory) {
        Optional<Inventory> optionalInventory = inventoryRepo.findById(inventory.getProductId());
        Integer quantity = 0;
        if(optionalInventory.isPresent()){
            quantity = optionalInventory.get().getQuantity();
        }
        quantity += inventory.getQuantity();

        inventory.setQuantity(quantity);

        return inventoryRepo.save(inventory);
    }

    @Override
    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consumeOrder(ConsumerRecord<String,Order> record) {
        Order order = record.value();
        System.out.println("Order recieved " + order);

        boolean available = checkAndReserveOrder(order);

        if (available) {
            System.out.println("Inventory reserved for order: " + order.getOrderId());
        } else {
            System.out.println("Inventory insufficient for order: " + order.getOrderId());
        }
    }

    private boolean checkAndReserveOrder(Order order) {
        List<Product> products = order.getProducts();
        boolean isAvailable = true;

       for(Product product : products) {
           Optional<Inventory> optionalProduct = inventoryRepo.findById(product.getProductId());
           if (optionalProduct.isPresent()) {
               Integer quantityFromInventory = optionalProduct.get().getQuantity();
               Integer quantityFromOrderReceived = product.getQuantity();

               if(quantityFromInventory < quantityFromOrderReceived){
                   isAvailable = false;
                   break;
               }
           } else {
               isAvailable = false;
               break;
           }
       }

       if(isAvailable){
           products.forEach(product -> {
               Integer quantityFromInventory = inventoryRepo.findById(product.getProductId()).get().getQuantity();
               Integer quantityFromOrderReceived = product.getQuantity();

               Inventory inventory = new Inventory();
               inventory.setProductId(product.getProductId());
               inventory.setQuantity(quantityFromInventory - quantityFromOrderReceived);

               inventoryRepo.save(inventory);
           });
           return true;
       }
       else{
           return false;
       }
    }
}
