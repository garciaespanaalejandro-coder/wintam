package com.wintam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmAttendanceRequest {
    @NotNull
    private Long cataId;
    @NotBlank
    private String code;
}
