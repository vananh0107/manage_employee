package com.vandev.manage.service;

import java.util.List;

public interface DepartmentService {
    com.vandev.manage.pojo.Department createDepartment(com.vandev.manage.pojo.Department department);
    com.vandev.manage.pojo.Department updateDepartment(Integer departmentId, com.vandev.manage.pojo.Department department);
    void deleteDepartment(Integer departmentId);
    com.vandev.manage.pojo.Department getDepartmentById(Integer departmentId);
    List<com.vandev.manage.pojo.Department> getAllDepartments();
}
