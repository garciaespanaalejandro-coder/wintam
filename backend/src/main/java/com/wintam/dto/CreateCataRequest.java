package com.wintam.dto;

import com.wintam.model.ExperienceLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateCataRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String wineType;

    @NotNull
    private ExperienceLevel experienceLevel;

    @NotBlank
    private String location;

    @NotNull
    private LocalDate scheduleDate;

    @NotNull
    private LocalTime scheduledTime;

    @NotNull
    private Integer maxAttendees;
}
