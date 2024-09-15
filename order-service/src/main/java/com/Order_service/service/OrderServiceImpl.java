package com.Order_service.service;

import com.Order_service.dto.InventoryUpdate;
import com.Order_service.dto.OrderRequest;
import com.Order_service.dto.OrderResponse;
import com.Order_service.entity.Order;
import com.Order_service.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.Order_service.repo.OrderRepository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService{
    private static final String ORDER_TOPIC = "order-topic";
    private static final String DISTINCT_CITIES = "distinctCities";

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

    @Override
    public List<String> getDistinctCities() {
        try(Jedis jedis = new Jedis("localhost", 6379)) {
            List<String> distinctCities = jedis.lrange(DISTINCT_CITIES, 0, -1);

            if(distinctCities.size() != 0){
                return distinctCities;
            }
            else {
                distinctCities = orderRepository.findDistinctCities();
                distinctCities.forEach(order -> {
                    jedis.rpush(DISTINCT_CITIES,order);
                });
                jedis.expire(DISTINCT_CITIES, 30);

                return distinctCities;
            }
        }
    }
}
