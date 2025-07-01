package org.example.pisproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Integer id;
    private Integer userId;
    private List<CartItemDTO> items;
}
