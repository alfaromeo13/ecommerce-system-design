package org.example.pisproject.mapper;

import org.example.pisproject.dto.UserDTO;
import org.example.pisproject.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO usersDTO);
    UserDTO toDTO(User user);
    List<UserDTO> toDTOList(List<User> user);
}
