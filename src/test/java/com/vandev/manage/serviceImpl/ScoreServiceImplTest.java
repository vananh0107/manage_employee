package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Score;
import com.vandev.manage.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScoreServiceImplTest {
    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreServiceImpl scoreServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createScore_Success() {
        Score score = new Score();
        Date recordedDate = java.sql.Date.valueOf(LocalDate.now());
        score.setRecordedDate(recordedDate);
        score.setReason("work late");
        when(scoreRepository.save(score)).thenReturn(score);

        Score result = scoreServiceImpl.createScore(score);

        assertNotNull(result);
        verify(scoreRepository, times(1)).save(score);
    }

    @Test
    void createScore_MissingReason_ThrowsValidationException() {
        Score score = new Score();
        Date recordedDate = java.sql.Date.valueOf(LocalDate.now());
        score.setRecordedDate(recordedDate);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreServiceImpl.createScore(score);
        });

        assertEquals("Reason is required.", exception.getMessage());
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    void createScore_MissingRecordedDate_ThrowsValidationException() {
        Score score = new Score();
        score.setReason("Work late");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreServiceImpl.createScore(score);
        });

        assertEquals("Recorded date is required.", exception.getMessage());
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    void updateScore_ScoreExists_Success() {
        Score existingScore = new Score();
        existingScore.setId(1);
        Score updatedScore = new Score();
        updatedScore.setReason("Work late");

        when(scoreRepository.findById(1)).thenReturn(Optional.of(existingScore));
        when(scoreRepository.save(existingScore)).thenReturn(existingScore);

        Score result = scoreServiceImpl.updateScore(1, updatedScore);

        assertNotNull(result);
        assertEquals("Work late", existingScore.getReason());
        verify(scoreRepository, times(1)).findById(1);
        verify(scoreRepository, times(1)).save(existingScore);
    }

    @Test
    void updateScore_ScoreNotFound_ThrowsException() {
        Score updatedScore = new Score();
        when(scoreRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreServiceImpl.updateScore(1, updatedScore);
        });

        assertEquals("Score record not found.", exception.getMessage());
        verify(scoreRepository, times(1)).findById(1);
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    void deleteScore_ScoreExists_Success() {
        when(scoreRepository.existsById(1)).thenReturn(true);

        scoreServiceImpl.deleteScore(1);

        verify(scoreRepository, times(1)).existsById(1);
        verify(scoreRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteScore_ScoreNotFound_ThrowsException() {
        when(scoreRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreServiceImpl.deleteScore(1);
        });

        assertEquals("Point record not found.", exception.getMessage());
        verify(scoreRepository, times(1)).existsById(1);
        verify(scoreRepository, never()).deleteById(1);
    }

    @Test
    void getScoreById_ScoreExists_Success() {
        Score score = new Score();
        score.setId(1);
        when(scoreRepository.findById(1)).thenReturn(Optional.of(score));

        Score result = scoreServiceImpl.getScoreById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(scoreRepository, times(1)).findById(1);
    }

    @Test
    void getScoreById_ScoreNotFound_ThrowsException() {
        when(scoreRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreServiceImpl.getScoreById(1);
        });

        assertEquals("Point record not found.", exception.getMessage());
        verify(scoreRepository, times(1)).findById(1);
    }

    @Test
    void getScoreByEmployeeId_Success() {
        Integer employeeId = 1;
        List<Score> scores = Arrays.asList(new Score(), new Score());
        when(scoreRepository.findByEmployee_Id(employeeId)).thenReturn(scores);

        List<Score> result = scoreServiceImpl.getScoreByEmployeeId(employeeId);

        assertEquals(2, result.size());
        verify(scoreRepository, times(1)).findByEmployee_Id(employeeId);
    }

    @Test
    void getPagedScores_Success() {
        Page<Score> page = new PageImpl<>(Arrays.asList(new Score(), new Score()));
        when(scoreRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Score> result = scoreServiceImpl.getPagedScores(PageRequest.of(0, 8));

        assertEquals(2, result.getTotalElements());
        verify(scoreRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchScoreByEmployeeFullName_Success() {
        Page<Score> page = new PageImpl<>(Arrays.asList(new Score(), new Score()));
        when(scoreRepository.findByEmployeeFullNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Score> result = scoreServiceImpl.searchScoreByEmployeeFullName("John", PageRequest.of(0, 2));

        assertEquals(2, result.getTotalElements());
        verify(scoreRepository, times(1)).findByEmployeeFullNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}