package com.wintam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String reason;
}
