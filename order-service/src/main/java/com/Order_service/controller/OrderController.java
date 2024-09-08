package com.Order_service.controller;

import com.Order_service.dto.OrderRequest;
import com.Order_service.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Order_service.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse savedOrder = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(savedOrder, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String get(){
        return "Get is working...";
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderStatus(@PathVariable Long orderId){
        OrderResponse order = orderService.getOrderStatus(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
