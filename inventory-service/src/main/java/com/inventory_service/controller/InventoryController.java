package com.inventory_service.controller;

import com.inventory_service.entity.Inventory;
import com.inventory_service.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String get(){
        return "Inventory Test Successfull....";
    }

    @PutMapping
//  If product doesn't exist it creates it or else increases count.
    public String addUpdateProduct(@RequestBody Inventory inventory){
        inventoryService.addProduct(inventory);
        return "Added/Updated product successfully...";
    }

    @GetMapping("/getOutOfStockProducts")
    public List<String> getOutOfStockProducts(){
        return inventoryService.getOutOfStockProducts();
    }
}
