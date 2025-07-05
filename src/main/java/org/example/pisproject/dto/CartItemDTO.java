package org.example.pisproject.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartItemDTO implements Serializable {
    private Long id;
    private Long productId;
    private Long userId;
    private String productName;
    private Integer quantity;
}