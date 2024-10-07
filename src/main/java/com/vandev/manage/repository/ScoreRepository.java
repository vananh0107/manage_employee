package com.vandev.manage.repository;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findByEmployee_Id(Integer employeeId);
    List<Score> findAllByOrderByRecordedDateDesc();
    @Query("SELECT e FROM Employee e " +
            "JOIN e.scores s " +
            "GROUP BY e.id " +
            "ORDER BY (COUNT(CASE WHEN s.type = true THEN 1 END) - COUNT(CASE WHEN s.type = false THEN 1 END)) DESC")
    List<Employee> findTop10EmployeesByRewardPoints(Pageable pageable);
}