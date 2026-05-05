package com.wintam.dto;

import com.wintam.model.CataStatus;
import com.wintam.model.ExperienceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CataResponse {
    public CataResponse(){}
    private Long id;
    private CataStatus cataStatus;
    private String hostUsername;
    private String wineType;
    private String title;
    private ExperienceLevel experienceLevel;
    private String location;
    private LocalDate scheduleDate;
    private LocalTime scheduledTime;
    private Integer maxAttendees;
}