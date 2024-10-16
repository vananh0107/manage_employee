package com.vandev.manage.service;

import com.vandev.manage.dto.DepartmentSummaryDTO;
import com.vandev.manage.dto.EmployeeSummaryDTO;
import com.vandev.manage.pojo.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScoreService {
    Score createScore(Score score);
    Score updateScore(Integer pointId, Score score);
    void deleteScore(Integer pointId);
    Score getScoreById(Integer pointId);
    List<Score> getScoreByEmployeeId(Integer employeeId);
    Page<Score> getPagedScores(Pageable pageable);
    Page<Score> searchScoreByEmployeeFullName(String fullName,Pageable pageable);
    Page<DepartmentSummaryDTO> getDepartmentSummary(int page);
    Page<EmployeeSummaryDTO> getEmployeeSummary(int page);
}
