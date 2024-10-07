package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Score;
import com.vandev.manage.repository.ScoreRepository;
import com.vandev.manage.service.ScoreService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public Score createScore(Score score) {
        if (score.getReason() == null || score.getReason().trim().isEmpty()) {
            throw new ValidationException("Reason is required.");
        }
        if (score.getRecordedDate() == null) {
            throw new ValidationException("Recorded date is required.");
        }
        return scoreRepository.save(score);
    }

    @Override
    public Score updateScore(Integer pointId, Score score) {
        Optional<Score> existingPoint = scoreRepository.findById(pointId);
        if (existingPoint.isPresent()) {
            Score updatedScore = existingPoint.get();
            updatedScore.setType(score.isType());
            updatedScore.setReason(score.getReason());
            updatedScore.setRecordedDate(score.getRecordedDate());
            updatedScore.setEmployee(score.getEmployee());
            return scoreRepository.save(updatedScore);
        } else {
            throw new IllegalArgumentException("Point record not found.");
        }
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
    public List<Score> getAllScore() {
        return scoreRepository.findAll();
    }


    @Override
    public List<Score> getScoreByEmployeeId(Integer employeeId) {
        return scoreRepository.findByEmployee_Id(employeeId);
    }
    @Override
    public List<Score> getAllScoresSortedByDate(){
        return scoreRepository.findAllByOrderByRecordedDateDesc();
    }
    @Override
    public Page<Score> getPagedScores(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        return scoreRepository.findAll(fixedPageable);
    }
}