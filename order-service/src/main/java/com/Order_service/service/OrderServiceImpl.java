package com.Order_service.service;

import com.Order_service.dto.InventoryUpdate;
import com.Order_service.dto.OrderRequest;
import com.Order_service.dto.OrderResponse;
import com.Order_service.entity.Order;
import com.Order_service.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.Order_service.repo.OrderRepository;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
    private static final String ORDER_TOPIC = "order-topic";
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String,Order> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, Order> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setProducts(orderRequest.getProducts());
        order.setAddress(orderRequest.getAddress());
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order);

        kafkaTemplate.send(ORDER_TOPIC,savedOrder);

        return new OrderResponse(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    @Override
    public OrderResponse getOrderStatus(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            OrderResponse response = new OrderResponse();
            response.setOrderId(order.getOrderId());
            response.setStatus(order.getStatus());

            return response;
        }
        throw new ResourceNotFoundException("Order with id: " + orderId + " was not found..");
    }

    @Override
    @KafkaListener(topics = "inventoryUpdate-topic",groupId = "order-group")
    public void updateOrderBasedOnInventory(InventoryUpdate inventoryUpdate) {
        Optional<Order> optionalOrder = orderRepository.findById(inventoryUpdate.getOrderId());
        Order order = optionalOrder.get();
        order.setStatus(inventoryUpdate.getStatus());

        Order updatedOrder = orderRepository.save(order);
        System.out.println("Updated Order: "+ updatedOrder);
    }
}
