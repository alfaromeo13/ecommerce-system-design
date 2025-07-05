package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.CartItem;
import org.example.pisproject.entity.Product;
import org.example.pisproject.entity.ShoppingCart;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.ProductRepository;
import org.example.pisproject.repository.ShoppingCartRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepo;
    private final ProductRepository productRepository;

    @Transactional
    @CachePut(value = "userCarts", key = "#userId")
    public ShoppingCart createCartForUser(Long userId) {
        return cartRepo.findByUserId(userId).orElseGet(() -> {
            ShoppingCart cart = new ShoppingCart();

            User user = new User();
            user.setId(userId);
            cart.setUser(user);

            cart.setOrderDate(LocalDate.now());
            cart.setItems(new ArrayList<>());

            return cartRepo.save(cart);
        });
    }

    @Transactional
    @CachePut(value = "userCarts", key = "#userId")
    public ShoppingCart addItemToCart(Long userId, Long productId, int quantity) {
        ShoppingCart cart = cartRepo.findByUserId(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setUser(cart.getUser());
        item.setQuantity(quantity);
        cart.getItems().add(item);
        return cartRepo.save(cart); // cascades the item :D
    }

    @Transactional
    @CachePut(value = "userCarts", key = "#userId") // Update the whole cart with one item removed
    public ShoppingCart removeItemFromCart(Long userId, Long itemId) {
        ShoppingCart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().removeIf(item -> item.getId().equals(itemId)); // Remove item in-memory
        return cartRepo.save(cart); // cascades the item
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userCarts", key = "#userId")
    public Optional<ShoppingCart> getCartByUserId(Long userId) {
        return cartRepo.findByUserId(userId);
    }

}