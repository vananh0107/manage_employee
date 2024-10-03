package com.vandev.manage.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Mã phòng ban

    @Column(unique = true, nullable = false)
    private String name; // Tên phòng ban
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
