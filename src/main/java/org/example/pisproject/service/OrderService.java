package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.Order;
import org.example.pisproject.entity.OrderItem;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    @Transactional
    public Order placeOrder(Long userId, Order order) {
        User user = new User();
        user.setId(userId);
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        return orderRepo.save(order);
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
