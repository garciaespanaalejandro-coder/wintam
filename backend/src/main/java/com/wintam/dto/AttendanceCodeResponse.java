package com.wintam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AttendanceCodeResponse {
    private String code;
    private LocalDateTime generatedAt;
}
