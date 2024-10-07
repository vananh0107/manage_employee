package com.vandev.manage.repository;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository  extends JpaRepository<Employee,Integer> {
    List<Employee> findByDepartment(Department department);
    List<Employee> findByDepartmentIsNull();
    @Query("SELECT e FROM Employee e JOIN e.scores s " +
            "GROUP BY e.id " +
            "ORDER BY (COUNT(CASE WHEN s.type = true THEN 1 END) - COUNT(CASE WHEN s.type = false THEN 1 END)) DESC")
    List<Employee> findTop10EmployeesByRewardPoints(Pageable pageable);
    List<Employee> findByUsersIsNull();
}