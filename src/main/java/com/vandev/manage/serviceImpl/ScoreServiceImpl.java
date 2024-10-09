package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Score;
import com.vandev.manage.repository.ScoreRepository;
import com.vandev.manage.service.ScoreService;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private  ScoreRepository scoreRepository;

    @Override
    public Score createScore(Score score) {
        if (score.getReason() == null || score.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason is required.");
        }
        if (score.getRecordedDate() == null) {
            throw new IllegalArgumentException("Recorded date is required.");
        }
        return scoreRepository.save(score);
    }

    @Override
    public Score updateScore(Integer scoreId, Score score) {
        return scoreRepository.findById(scoreId)
                .map(existingScore -> {
                    BeanUtils.copyProperties(score, existingScore, "id", "employee");
                    return scoreRepository.save(existingScore);
                })
                .orElseThrow(() -> new IllegalArgumentException("Score record not found."));
    }

    @Override
    public void deleteScore(Integer pointId) {
        if (!scoreRepository.existsById(pointId)) {
            throw new IllegalArgumentException("Point record not found.");
        }
        scoreRepository.deleteById(pointId);
    }

    @Override
    public Score getScoreById(Integer pointId) {
        return scoreRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Point record not found."));
    }


    @Override
    public List<Score> getScoreByEmployeeId(Integer employeeId) {
        return scoreRepository.findByEmployee_Id(employeeId);
    }

    @Override
    public Page<Score> getPagedScores(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        return scoreRepository.findAll(fixedPageable);
    }
    @Override
    public Page<Score> searchScoreByEmployeeFullName(String fullName, Pageable pageable) {
        return scoreRepository.findByEmployeeFullNameContainingIgnoreCase(fullName, pageable);
    }
}