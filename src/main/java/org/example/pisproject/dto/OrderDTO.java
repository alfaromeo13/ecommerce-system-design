package org.example.pisproject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDTO> items;
}
