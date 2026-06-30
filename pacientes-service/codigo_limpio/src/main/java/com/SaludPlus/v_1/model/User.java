package com.SaludPlus.v_1.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // Nombre de la tabla en MySQL
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}