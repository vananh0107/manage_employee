package com.vandev.manage.repository;

import com.vandev.manage.pojo.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    Optional<Department> findByName(String name);
}
