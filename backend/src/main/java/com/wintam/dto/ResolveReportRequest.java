package com.wintam.dto;

import com.wintam.model.SanctionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResolveReportRequest {
    @NotNull
    private Long reportId;
    @NotNull
    private SanctionType sanctionType;
}
