package org.example.pisproject.mapper;

import org.example.pisproject.dto.CartItemDTO;
import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.entity.CartItem;
import org.example.pisproject.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(source = "items", target = "items")
    ShoppingCartDTO toDTO(ShoppingCart cart);

    ShoppingCart toEntity(ShoppingCartDTO dto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "user.id", target = "userId")
    CartItemDTO toItemDTO(CartItem item);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "user.id", source = "userId")
    CartItem toEntity(CartItemDTO dto);

    List<CartItemDTO> toItemDTOList(List<CartItem> items);

    List<CartItem> toItemEntityList(List<CartItemDTO> dtos);
}