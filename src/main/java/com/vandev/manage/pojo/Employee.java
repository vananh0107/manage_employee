package com.vandev.manage.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Date;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Mã nhân viên

    @NotNull(message = "Full name is required")
    private String fullName; // Họ và tên

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "Male|Female", message = "Gender must be Male, Female")
    private String gender;

    @NotNull(message = "Image URL is required")
    private String image; // Hình ảnh

    @NotNull(message = "Birthdate is required")
    private Date birthDate; // Ngày sinh

    @NotNull(message = "Salary is required")
    private double salary; // Lương

    @NotNull(message = "Level is required")
    @Size(min = 1, max = 10, message = "Level must be between 1 and 10")
    private int level; // Cấp độ

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email; // Email

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{8}$", message = "Phone number is invalid")
    private String phone; // Điện thoại

    private String notes; // Ghi chú

    @ManyToOne
    @JoinColumn(name = "department_id",nullable = true)
    private Department department;
}
