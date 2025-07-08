package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.OrderDTO;
import org.example.pisproject.entity.Order;
import org.example.pisproject.entity.OrderItem;
import org.example.pisproject.entity.Product;
import org.example.pisproject.entity.User;
import org.example.pisproject.mapper.OrderMapper;
import org.example.pisproject.repository.OrderRepository;
import org.example.pisproject.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    /*
        NOTE: We cache DTOs instead of entities to avoid LazyInitializationException. This ensures that all needed data
        is fully initialized before caching, since entities rely on the persistence context which is not available during deserialization.
        Caching an entity means you might cache an object with uninitialized lazy-loaded fields.
        Later, when you deserialize that entity from the cache, JPA/Hibernate is not managing it anymore, so any attempt to load those lazy fields fails.
     */

    private final OrderRepository orderRepo;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepo;

    @Transactional
    @CachePut(value = "orders", key = "#result.id")
    @CacheEvict(value = "ordersByUser", key = "#userId")
    public OrderDTO placeOrder(Long userId, Order order) {
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

        Order saved = orderRepo.save(order);
        // about above line: All OrderItem entities attached to the Order will be automatically persisted, updated, or deleted when you save the Order.
        // hibernate will then cascade that operation to persist all OrderItems in order.getItems(). Because in Order class we have CascadeType.ALL
        // Because of cascade = ALL → item1 and item2 are saved automatically.If you didn’t set cascade, you’d have to save each OrderItem manually.
        return orderMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "#id")
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepo.findByIdWithItemsAndProducts(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "ordersByUser", key = "#userId")
    public List<OrderDTO> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepo.findByUserIdWithItemsAndProducts(userId);
        return orderMapper.toDTOList(orders);
    }
}