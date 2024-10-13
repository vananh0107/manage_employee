package com.vandev.manage.service;

import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.pojo.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    ScoreDTO createScore(ScoreDTO scoreDTO);
    ScoreDTO updateScore(Integer pointId, ScoreDTO scoreDTO);
    void deleteScore(Integer pointId);
    ScoreDTO getScoreById(Integer pointId);
    List<ScoreDTO> getScoreByEmployeeId(Integer employeeId);
    Page<ScoreDTO> getPagedScores(Pageable pageable);
    Page<ScoreDTO> searchScoreByEmployeeFullName(String fullName,Pageable pageable);
    Page<Map<String, Object>> getDepartmentSummary(int page);
    Page<Map<String, Object>> getEmployeeSummary(int page);

}
