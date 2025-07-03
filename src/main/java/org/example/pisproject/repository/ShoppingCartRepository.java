package org.example.pisproject.repository;

import org.example.pisproject.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.id = :itemId")
    void deleteCartItemById(Long itemId);
}