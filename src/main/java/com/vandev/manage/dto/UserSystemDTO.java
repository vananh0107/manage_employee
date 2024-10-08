package com.vandev.manage.dto;

import com.vandev.manage.pojo.Employee;
import lombok.Data;

@Data
public class UserSystemDTO {
    private Integer id;
    private String username;
    private Employee employee;
    private String role;
    private Boolean active=false;
}
