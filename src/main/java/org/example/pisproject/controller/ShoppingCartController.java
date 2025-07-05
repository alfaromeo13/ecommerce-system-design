package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.CartItemDTO;
import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<ShoppingCartDTO> createCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.createCartForUser(userId));
    }

    @PostMapping("/{userId}/item")
    public ResponseEntity<ShoppingCartDTO> addItem(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, cartItemDTO));
    }

    @DeleteMapping("user/{userId}/item/{itemId}")
    public ResponseEntity<ShoppingCartDTO> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId,itemId));
    }

    @GetMapping("/user/{userId}")
    public ShoppingCartDTO getByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }
}