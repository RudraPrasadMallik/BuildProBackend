package com.buildpro.orderservice.service;

import com.buildpro.orderservice.entity.Order;
import com.buildpro.orderservice.entity.OrderItem;
import com.buildpro.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Order placeOrder(Order order, String userId) {
        // Set user ID and date
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());

        // Link items to order
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }

        // Calculate total amount
        double total = order.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(total);

        return orderRepo.save(order);
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepo.findByUserId(userId);
    }
}
