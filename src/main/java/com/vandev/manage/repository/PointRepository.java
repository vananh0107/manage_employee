package com.vandev.manage.repository;

import com.vandev.manage.pojo.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Integer> {
    List<Point> findByEmployee_Id(Integer employeeId);
}