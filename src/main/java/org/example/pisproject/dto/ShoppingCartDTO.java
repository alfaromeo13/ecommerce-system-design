package org.example.pisproject.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ShoppingCartDTO {
    private Long id;
    private UserDTO user;
    private LocalDate createdAt;
    private List<CartItemDTO> items;
}