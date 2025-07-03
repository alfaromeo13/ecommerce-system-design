package org.example.pisproject.mapper;

import org.example.pisproject.dto.ProductDTO;
import org.example.pisproject.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTOList(List<Product> products);
}
