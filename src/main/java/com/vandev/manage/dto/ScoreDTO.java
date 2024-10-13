package com.vandev.manage.dto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class ScoreDTO {
    private Integer id;
    private boolean type;
    private String reason;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date recordedDate;
    private Integer employeeId;
}
