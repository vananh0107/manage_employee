package com.vandev.manage.dto;

import com.vandev.manage.pojo.Employee;
import lombok.Data;

import java.util.List;
@Data
public class DepartmentDTO {
    private Integer id;
    private String name;
    private List<Employee> employees;
}
