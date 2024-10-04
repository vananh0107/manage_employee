package com.vandev.manage.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class UserSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 8, message = "Password min 8 character")
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    @NotNull(message = "Role is required")
    private String role;

    @Column(nullable = false)
    private Boolean active=false;
}

