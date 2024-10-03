package com.vandev.manage.repository;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository  extends JpaRepository<Employee,Integer> {
    Page<Employee> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    List<Employee> findByDepartment(Department department);
}