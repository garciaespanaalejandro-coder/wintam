package com.wintam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String name;
    private String username;
    private String surname;
    private String email;
    private String description;
    private Integer karma;
}
