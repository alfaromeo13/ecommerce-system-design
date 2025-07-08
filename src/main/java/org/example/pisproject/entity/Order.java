package org.example.pisproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private String status;

    @Column(name = "created_at")
    private LocalDate orderDate;

    // When you persist, update, or delete an Order, the same operation is automatically cascaded to its OrderItem list.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    /*
        ALL	    Applies all cascade types below
        PERSIST	When you persist() the parent, the child is also saved
        MERGE	When you merge() the parent, the child is updated too
        REMOVE	When you remove() the parent, the child is deleted too
        REFRESH	If you refresh() the parent from DB, the child is refreshed too
        DETACH	When the parent is detached from EntityManager, so is the child

     */
}