package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.Order;
import org.example.pisproject.entity.OrderItem;
import org.example.pisproject.entity.Product;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.OrderRepository;
import org.example.pisproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    @Transactional
    public Order placeOrder(Long userId, Order order) {
        User user = new User();
        user.setId(userId);
        order.setUser(user);

        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order); // Required for the cascade to correctly link them
                item.setUser(user);   // this is necessary for sharding
                Product product = productRepo.findById(item.getProduct().getId()).orElse(null);
                item.setProduct(product);
            }
        }

        return orderRepo.save(order);
        // about above line: All OrderItem entities attached to the Order will be automatically persisted, updated, or deleted when you save the Order.
        // hibernate will then cascade that operation to persist all OrderItems in order.getItems(). Because in Order class we have  CascadeType.ALL
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }
}