package com.vandev.manage.service;

import com.vandev.manage.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Integer employeeId, Employee employee);
    void deleteEmployee(Integer employeeId);
    Employee getEmployeeById(Integer employeeId);
    List<Employee> getAllEmployees();
    Page<Employee> getPagedEmployees(String name, Pageable pageable);
}
