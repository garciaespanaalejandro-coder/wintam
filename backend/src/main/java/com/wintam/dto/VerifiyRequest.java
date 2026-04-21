package com.wintam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifiyRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}
