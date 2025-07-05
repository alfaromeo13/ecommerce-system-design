package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pisproject.dto.CartItemDTO;
import org.example.pisproject.dto.ShoppingCartDTO;
import org.example.pisproject.entity.CartItem;
import org.example.pisproject.entity.Product;
import org.example.pisproject.entity.ShoppingCart;
import org.example.pisproject.entity.User;
import org.example.pisproject.mapper.ShoppingCartMapper;
import org.example.pisproject.repository.ProductRepository;
import org.example.pisproject.repository.ShoppingCartRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartMapper cartMapper;
    private final ShoppingCartRepository cartRepo;
    private final ProductRepository productRepository;

    @Transactional
    @CachePut(value = "userCarts", key = "#userId")
    public ShoppingCartDTO createCartForUser(Long userId) {
        return cartMapper.toDTO(cartRepo.findByUserIdWithItems(userId).orElseGet(() -> {
            ShoppingCart cart = new ShoppingCart();

            User user = new User();
            user.setId(userId);
            cart.setUser(user);

            cart.setOrderDate(LocalDate.now());
            cart.setItems(new ArrayList<>());

            return cartRepo.save(cart);
        }));
    }

    @Transactional
    @CachePut(value = "userCarts", key = "#userId")
    public ShoppingCartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        ShoppingCart cart = cartRepo.findByUserIdWithItems(userId).orElseThrow();
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElse(null);
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setUser(cart.getUser());
        item.setQuantity(cartItemDTO.getQuantity());
        cart.getItems().add(item);
        return cartMapper.toDTO(cartRepo.save(cart)); // cascades the item :D
    }

    @Transactional
    @CachePut(value = "userCarts", key = "#userId") // Update the whole cart with one item removed
    public ShoppingCartDTO removeItemFromCart(Long userId, Long itemId) {
        ShoppingCart cart = cartRepo.findByUserIdWithItems(userId).orElseThrow();
        cart.getItems().removeIf(item -> item.getId().equals(itemId)); // Remove item in-memory
        return cartMapper.toDTO(cartRepo.save(cart)); // cascades the item
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userCarts", key = "#userId")
    public ShoppingCartDTO getCartByUserId(Long userId) {
        return cartMapper.toDTO(cartRepo.findByUserIdWithItems(userId).orElseThrow());
    }
}