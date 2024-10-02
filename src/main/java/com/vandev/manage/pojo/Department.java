package com.vandev.manage.pojo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer department_id; // Mã phòng ban

    @Column(unique = true, nullable = false)
    private String name; // Tên phòng ban
}
