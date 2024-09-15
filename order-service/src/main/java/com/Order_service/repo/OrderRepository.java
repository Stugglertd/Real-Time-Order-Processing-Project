package com.Order_service.repo;

import com.Order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT DISTINCT o.address.city FROM Order o")
    List<String> findDistinctCities();
}
