package org.example.pisproject.mapper;

import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.entity.ShoppingCart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDTO toDTO(ShoppingCart cart);
    ShoppingCart toEntity(ShoppingCartDTO cartDTO);
    List<ShoppingCartDTO> toDTOList(List<ShoppingCart> cart);
}