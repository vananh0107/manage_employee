package com.vandev.manage.service;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employee);
    EmployeeDTO updateEmployee(Integer employeeId, EmployeeDTO employeeDTO);
    void deleteEmployee(Integer employeeId);
    EmployeeDTO getEmployeeById(Integer employeeId);
    List<EmployeeDTO> getAllEmployees();
    Page<EmployeeDTO> getPagedEmployees( Pageable pageable);
    List<EmployeeDTO> getEmployeesWithoutDepartment();
    List<EmployeeDTO> findAllById(List<Integer> ids);
    void saveAll(List<EmployeeDTO> employees);
    List<EmployeeDTO> getEmployeesByDepartment(DepartmentDTO departmentDTO);
    List<EmployeeDTO> getTopEmployees();
    List<EmployeeDTO> getEmployeesWithoutUser();
    Page<EmployeeDTO> searchByFullName(String fullName, Pageable pageable);
}
