package org.example.pisproject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate orderDate;
    private List<OrderItemDTO> items;
}