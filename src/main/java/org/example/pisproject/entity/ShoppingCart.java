package org.example.pisproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
@Data
public class ShoppingCart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDate orderDate;

    // If a CartItem is removed from the list (cart.getItems().remove(item)), it will also be deleted from the DB!
    // In other class, CascadeType.REMOVE only applies when you explicitly delete the parent. All OrderItems will also be deleted, because the entire order is being removed.
    // But what if you just remove a child from the list? The item is removed from memory, but not deleted from the database.
    // orphanRemoval = true means: If a child entity is removed from the collection in the parent (e.g., List<OrderItem>), it should be automatically deleted from the database.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;
    /*
    Use CascadeType.ALL + orphanRemoval = true when:
        ~ You want children to be deleted if removed from parent collection
        ~ And also deleted when the parent is deleted
     */
}