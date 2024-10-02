package com.vandev.manage.repository;

import com.vandev.manage.pojo.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository  extends JpaRepository<Employee,Integer> {
    List<Employee> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

}