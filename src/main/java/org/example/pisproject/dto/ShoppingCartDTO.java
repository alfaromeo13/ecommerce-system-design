package org.example.pisproject.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ShoppingCartDTO implements Serializable {
    private Long id;
    private UserDTO user;
    private LocalDate orderDate;
    private List<CartItemDTO> items;
}