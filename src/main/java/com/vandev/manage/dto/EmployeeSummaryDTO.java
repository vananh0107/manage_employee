package com.vandev.manage.dto;

import lombok.Data;

@Data
public class EmployeeSummaryDTO {
    private String employeeName;
    private Long totalAchievements;
    private Long totalDisciplines;
    private Long rewardScore;
    public EmployeeSummaryDTO(String employeeName, Long totalAchievements, Long totalDisciplines, Long rewardScore) {
        this.employeeName = employeeName;
        this.totalAchievements = totalAchievements;
        this.totalDisciplines = totalDisciplines;
        this.rewardScore = rewardScore;
    }
}
