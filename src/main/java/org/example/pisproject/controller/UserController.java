package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.UsersDTO;
import org.example.pisproject.entity.User;
import org.example.pisproject.mapper.UserMapper;
import org.example.pisproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // todo: keshiranje zavrsi implementaciju i podigni redis
    // todo: implementiraj servise + kopntrolere + mapere do kaja
    // todo: napravi vise insatnci spring boot projekta u kubernetes

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UsersDTO> createUser(@RequestBody UsersDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(userMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UsersDTO> getAllUsers() {
        return userMapper.toDTOList(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @RequestBody UsersDTO dto) {
        User user = userService.updateUser(id, userMapper.toEntity(dto));
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}