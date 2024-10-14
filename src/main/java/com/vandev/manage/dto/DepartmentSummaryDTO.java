package com.vandev.manage.dto;

import lombok.Data;

@Data
public class DepartmentSummaryDTO {
    private String departmentName;
    private Long totalAchievements;
    private Long totalDisciplines;
    private Long rewardScore;
    public DepartmentSummaryDTO(String departmentName, Long totalAchievements, Long totalDisciplines, Long rewardScore) {
        this.departmentName = departmentName;
        this.totalAchievements = totalAchievements;
        this.totalDisciplines = totalDisciplines;
        this.rewardScore = rewardScore;
    }
}
