package org.example.pisproject.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CartDTO  implements Serializable {
    private Integer id;
    private Integer userId;
    private List<CartItemDTO> items;
}
