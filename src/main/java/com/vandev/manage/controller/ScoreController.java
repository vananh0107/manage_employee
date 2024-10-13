package com.vandev.manage.controller;

import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<ScoreDTO> createScore(@RequestBody ScoreDTO scoreDTO) {
        ScoreDTO createdScore = scoreService.createScore(scoreDTO);
        return ResponseEntity.ok(createdScore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoreDTO> updateScore(@PathVariable("id") Integer scoreId, @RequestBody ScoreDTO scoreDTO) {
        ScoreDTO updatedScore = scoreService.updateScore(scoreId, scoreDTO);
        return ResponseEntity.ok(updatedScore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable("id") Integer scoreId) {
        scoreService.deleteScore(scoreId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScoreDTO> getScoreById(@PathVariable("id") Integer scoreId) {
        ScoreDTO scoreDTO = scoreService.getScoreById(scoreId);
        return ResponseEntity.ok(scoreDTO);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ScoreDTO>> getScoresByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
        List<ScoreDTO> scores = scoreService.getScoreByEmployeeId(employeeId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ScoreDTO>> getPagedScores(Pageable pageable) {
        Page<ScoreDTO> pagedScores = scoreService.getPagedScores(pageable);
        return ResponseEntity.ok(pagedScores);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ScoreDTO>> searchScoresByEmployeeFullName(@RequestParam("fullName") String fullName, Pageable pageable) {
        Page<ScoreDTO> scores = scoreService.searchScoreByEmployeeFullName(fullName, pageable);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/department-summary")
    public ResponseEntity<Page<Map<String, Object>>> getDepartmentSummary(@RequestParam(defaultValue = "0") int page) {
        Page<Map<String, Object>> departmentSummary = scoreService.getDepartmentSummary(page);
        return ResponseEntity.ok(departmentSummary);
    }

    @GetMapping("/employee-summary")
    public ResponseEntity<Page<Map<String, Object>>> getEmployeeSummary(@RequestParam(defaultValue = "0") int page) {
        Page<Map<String, Object>> employeeSummary = scoreService.getEmployeeSummary(page);
        return ResponseEntity.ok(employeeSummary);
    }
}
