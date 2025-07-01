package org.example.pisproject.mapper;

import org.example.pisproject.dto.UsersDTO;
import org.example.pisproject.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UsersDTO usersDTO);
    UsersDTO toDTO(User user);
}
