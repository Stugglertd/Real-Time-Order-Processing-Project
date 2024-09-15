package com.inventory_service.repo;

import com.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    @Query("SELECT i.productId FROM Inventory i where i.quantity=0")
    List<String> findOutOfStockProducts();
}
