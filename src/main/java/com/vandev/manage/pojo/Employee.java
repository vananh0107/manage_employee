package com.vandev.manage.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "Male|Female", message = "Gender must be Male, Female")
    private String gender;

    private String image;
    @NotNull(message = "Birthdate is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotNull(message = "Salary is required")
    private double salary;

    @NotNull(message = "Level is required")
    @Min(1)
    @Max(10)
    private int level;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{8}$", message = "Phone number is invalid")
    private String phone;

    private String notes;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores;
    @ManyToOne
    @JoinColumn(name = "department_id",nullable = true)
    private Department department;
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private UserSystem user;
}
