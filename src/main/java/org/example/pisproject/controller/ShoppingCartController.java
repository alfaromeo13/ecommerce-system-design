package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.CartItemDTO;
import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.entity.ShoppingCart;
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
    public ResponseEntity<ShoppingCartDTO> createCart(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartMapper.toDTO(cartService.createCartForUser(userId)));
    }

    @PostMapping("/{userId}/item")
    public ResponseEntity<ShoppingCartDTO> addItem(@PathVariable Long userId, @RequestBody CartItemDTO dto) {
        ShoppingCart updated = cartService.addItemToCart(userId, dto.getProductId(), dto.getQuantity());
        return ResponseEntity.ok(shoppingCartMapper.toDTO(updated));
    }

    @DeleteMapping("user/{userId}/item/{itemId}")
    public ResponseEntity<ShoppingCartDTO> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(shoppingCartMapper.toDTO(cartService.removeItemFromCart(userId,itemId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ShoppingCartDTO> getByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId)
                .map(cart -> ResponseEntity.ok(shoppingCartMapper.toDTO(cart)))
                .orElse(ResponseEntity.notFound().build());
    }
}