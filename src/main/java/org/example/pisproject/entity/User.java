package org.example.pisproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    // Informs Hibernate that the ID will be generated externally (e.g. by ShardingSphere's Snowflake generator)
    private Long id;

    private String username;
    private String email;
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}