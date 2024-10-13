package com.vandev.manage.serviceImpl;

import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.repository.ScoreRepository;
import com.vandev.manage.service.ScoreService;
import com.vandev.manage.mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public ScoreDTO createScore(ScoreDTO scoreDTO) {
        if (scoreDTO.getReason() == null || scoreDTO.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason is required.");
        }
        if (scoreDTO.getRecordedDate() == null) {
            throw new IllegalArgumentException("Recorded date is required.");
        }

        Score score = scoreMapper.toEntity(scoreDTO);
        Score savedScore = scoreRepository.save(score);
        return scoreMapper.toDTO(savedScore);
    }

    @Override
    public ScoreDTO updateScore(Integer scoreId, ScoreDTO scoreDTO) {
        return scoreRepository.findById(scoreId)
                .map(existingScore -> {
                    BeanUtils.copyProperties(scoreDTO, existingScore, "id", "employee");
                    Score updatedScore = scoreRepository.save(existingScore);
                    return scoreMapper.toDTO(updatedScore);
                })
                .orElseThrow(() -> new IllegalArgumentException("Score record not found."));
    }

    @Override
    public void deleteScore(Integer scoreId) {
        if (!scoreRepository.existsById(scoreId)) {
            throw new IllegalArgumentException("Score record not found.");
        }
        scoreRepository.deleteById(scoreId);
    }

    @Override
    public ScoreDTO getScoreById(Integer scoreId) {
        return scoreRepository.findById(scoreId)
                .map(scoreMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Score record not found."));
    }

    @Override
    public List<ScoreDTO> getScoreByEmployeeId(Integer employeeId) {
        List<Score> scores = scoreRepository.findByEmployee_Id(employeeId);
        return scores.stream().map(scoreMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<ScoreDTO> getPagedScores(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        Page<Score> scorePage = scoreRepository.findAll(fixedPageable);
        return scorePage.map(scoreMapper::toDTO);
    }

    @Override
    public Page<ScoreDTO> searchScoreByEmployeeFullName(String fullName, Pageable pageable) {
        Page<Score> scorePage = scoreRepository.findByEmployeeFullNameContainingIgnoreCase(fullName, pageable);
        return scorePage.map(scoreMapper::toDTO);
    }

    @Override
    public Page<Map<String, Object>> getDepartmentSummary(int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return scoreRepository.getDepartmentSummarySortedByRewardScore(pageable);
    }

    @Override
    public Page<Map<String, Object>> getEmployeeSummary(int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return scoreRepository.getEmployeeSummarySortedByRewardScore(pageable);
    }
}
