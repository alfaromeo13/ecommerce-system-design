package org.example.pisproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // If your table has no AUTO_INCREMENT, the insert goes through ShardingSphere.

    private String username;
    private String email;
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;
}