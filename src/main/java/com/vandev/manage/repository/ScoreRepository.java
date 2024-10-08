package com.vandev.manage.repository;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findByEmployee_Id(Integer employeeId);
    @Query("SELECT e FROM Employee e " +
            "JOIN e.scores s " +
            "GROUP BY e.id " +
            "ORDER BY (COUNT(CASE WHEN s.type = true THEN 1 END) - COUNT(CASE WHEN s.type = false THEN 1 END)) DESC")
    List<Employee> findTopEmployeesByRewardPoints(Pageable pageable);
    @Query("SELECT s FROM Score s JOIN s.employee e WHERE LOWER(e.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    Page<Score> findByEmployeeFullNameContainingIgnoreCase(@Param("fullName") String fullName, Pageable pageable);}