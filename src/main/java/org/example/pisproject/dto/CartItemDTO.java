package org.example.pisproject.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private Long userId;
    private String productName;
    private Integer quantity;
}