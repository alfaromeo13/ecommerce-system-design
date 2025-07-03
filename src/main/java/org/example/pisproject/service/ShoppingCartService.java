package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.ShoppingCart;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.ShoppingCartRepository;
import org.example.pisproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepo;
    private final UserRepository userRepo;

    @Transactional
    public ShoppingCart createCartForUser(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        User user = new User();
        user.setId(userId);
        cart.setUser(user);
        cart.setOrderDate(LocalDate.now());
        return cartRepo.save(cart);
    }

    @Transactional(readOnly = true)
    public Optional<ShoppingCart> getCartByUserId(Long userId) {
        return cartRepo.findById(userId);
    }
}