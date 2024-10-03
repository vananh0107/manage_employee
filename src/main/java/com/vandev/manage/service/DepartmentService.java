package com.vandev.manage.service;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(com.vandev.manage.pojo.Department department);
    Department updateDepartment(Integer departmentId, com.vandev.manage.pojo.Department department);
    void deleteDepartment(Integer departmentId);
    Department getDepartmentById(Integer departmentId);
    List<Department> getAllDepartments();
    List<Employee> getEmployeesByDepartment(Department department);
}
