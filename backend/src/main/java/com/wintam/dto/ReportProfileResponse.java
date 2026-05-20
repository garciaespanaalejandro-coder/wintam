package com.wintam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReportProfileResponse {
    private Long id;
    private String reporter;
    private String reported;
    private String reason;
    private LocalDateTime date;
}
