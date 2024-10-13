package com.vandev.manage.dto;
import lombok.Data;

import java.util.Date;
@Data
public class EmployeeDTO {
    private Integer id;
    private String fullName;
    private String gender;
    private String image;
    private Date birthDate;
    private double salary;
    private int level;
    private String email;
    private String phone;
    private String notes;
    private Integer departmentId;
}
