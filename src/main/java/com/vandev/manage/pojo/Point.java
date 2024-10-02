package com.vandev.manage.pojo;

import jakarta.persistence.Table;

import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // ID ghi nhận
    private boolean type; // Loại: 1 là thành tích, 0 là kỷ luật
    private String reason; // Lý do
    private Date recordedDate; // Ngày ghi nhận
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "employee_id",nullable = false)
    private Employee employee;
}
