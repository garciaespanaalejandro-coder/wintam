package com.winetam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String idintifier;
    @NotBlank
    private String password;
}
