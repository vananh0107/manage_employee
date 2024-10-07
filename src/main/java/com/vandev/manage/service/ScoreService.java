package com.vandev.manage.service;

import com.vandev.manage.pojo.Score;

import java.util.List;

public interface ScoreService {
    Score createScore(Score score);
    Score updateScore(Integer pointId, Score score);
    void deleteScore(Integer pointId);
    Score getScoreById(Integer pointId);
    List<Score> getAllScore();
    List<Score> getScoreByEmployeeId(Integer employeeId);
    List<Score> getAllScoresSortedByDate();
}
