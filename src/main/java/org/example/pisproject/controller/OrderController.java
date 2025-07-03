package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.OrderDTO;
import org.example.pisproject.entity.Order;
import org.example.pisproject.mapper.OrderMapper;
import org.example.pisproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long userId, @RequestBody Order order) {
        return ResponseEntity.ok(orderMapper.toDTO(orderService.placeOrder(userId, order)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(orderMapper.toDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderMapper.toDTOList(orderService.getOrdersByUser(userId));
    }
}

