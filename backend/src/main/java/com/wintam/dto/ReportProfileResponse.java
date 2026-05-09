package com.wintam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReportProfileResponse {
    private String reporter;
    private String reported;
    private String reason;
    private LocalDateTime date;
}
