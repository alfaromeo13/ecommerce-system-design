package org.example.pisproject.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
}