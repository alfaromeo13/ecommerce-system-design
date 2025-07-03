package org.example.pisproject.mapper;

import org.example.pisproject.dto.OrderDTO;
import org.example.pisproject.dto.OrderItemDTO;
import org.example.pisproject.entity.Order;
import org.example.pisproject.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    @Mapping(source = "user.id", target = "userId")
    OrderDTO toDTO(Order order);

    @Mapping(target = "user.id", source = "userId")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOList(List<Order> orders);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemDTO toDTO(OrderItem item);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toEntity(OrderItemDTO dto);

    List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> items);
}