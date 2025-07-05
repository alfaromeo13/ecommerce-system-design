package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.OrderDTO;
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
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long userId, @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.placeOrder(userId, orderMapper.toEntity(orderDTO)));
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
}