package com.vandev.manage.service;

import com.vandev.manage.pojo.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(com.vandev.manage.pojo.Department department);
    Department updateDepartment(Integer departmentId, com.vandev.manage.pojo.Department department);
    void deleteDepartment(Integer departmentId);
    Department getDepartmentById(Integer departmentId);
    List<Department> getAllDepartments();
    Page<Department> findPaginated(int page, int size);
}
