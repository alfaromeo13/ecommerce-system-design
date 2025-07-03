package org.example.pisproject.mapper;

import org.example.pisproject.dto.OrderDTO;
import org.example.pisproject.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);
    Order toEntity(OrderDTO orderDTO);
    List<OrderDTO> toDTOList(List<Order> orders);
}