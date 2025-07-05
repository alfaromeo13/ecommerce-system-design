package org.example.pisproject.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.pisproject.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("""
            select distinct c from ShoppingCart c
            left join fetch c.items i
            left join fetch i.product
            where c.user.id = :userId
    """)
    Optional<ShoppingCart> findByUserIdWithItems(@Param("userId") Long userId);
    // We use LEFT JOIN FETCH to ensure that carts with no items are still returned (unlike an inner join which would
    // exclude them). DISTINCT is used to eliminate duplicates caused by joining with multiple items.
    // Just using JOIN FETCH would fail when the cart has no items, because JOIN FETCH behaves like an inner join,
    // which would exclude carts without items. LEFT JOIN FETCH avoids this issue by allowing empty collections.
}