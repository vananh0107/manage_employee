package com.vandev.manage.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = true) // FK to Employee table
    private Employee employee; // Link to Employee (if any)

    @NotNull(message = "Role is required")
    private String role; // Vai trò: user hoặc admin

    @Column(nullable = false)
    private Boolean active=false;
}

