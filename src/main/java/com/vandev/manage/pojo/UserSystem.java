package com.vandev.manage.pojo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="user")
public class UserSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
}

