package com.wintam.dto;

import com.wintam.model.CataStatus;
import com.wintam.model.ExperienceLevel;
import lombok.Data;

@Data
public class SearchCatasRequest {
    private String title;
    private String wineType;
    private ExperienceLevel experienceLevel;
    private String location;
    private CataStatus cataStatus;
}
