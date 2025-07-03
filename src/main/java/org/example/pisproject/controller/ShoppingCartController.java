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

    @PostMapping("/{userId}/items")
    public ResponseEntity<ShoppingCartDTO> addItem(@PathVariable Long userId, @RequestBody CartItemDTO dto) {
        ShoppingCart updated = cartService.addItemToCart(userId, dto.getProductId(), dto.getQuantity());
        return ResponseEntity.ok(shoppingCartMapper.toDTO(updated));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCartDTO> getCart(@PathVariable Long cartId) {
        return cartService.getCartByCartId(cartId)
                .map(cart -> ResponseEntity.ok(shoppingCartMapper.toDTO(cart)))
                .orElse(ResponseEntity.notFound().build());
    }
}