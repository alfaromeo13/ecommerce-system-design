package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.mapper.ShoppingCartMapper;
import org.example.pisproject.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ShoppingCartMapper shoppingCartMapper;

    @PostMapping("/{userId}")
    public ResponseEntity<ShoppingCartDTO> create(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartMapper.toDTO(cartService.createCartForUser(userId)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDTO> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId)
                .map(cart -> ResponseEntity.ok(shoppingCartMapper.toDTO(cart)))
                .orElse(ResponseEntity.notFound().build());
    }
}