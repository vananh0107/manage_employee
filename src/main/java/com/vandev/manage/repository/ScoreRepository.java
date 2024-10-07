package com.vandev.manage.repository;

import com.vandev.manage.pojo.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findByEmployee_Id(Integer employeeId);
    List<Score> findAllByOrderByRecordedDateDesc();
}