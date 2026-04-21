package com.wintam.service;

import com.wintam.dto.AuthResponse;
import com.wintam.dto.LoginRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.dto.RegisterRequest;

public interface AuthService {
    public MessageResponse createAccount(RegisterRequest request);
    public AuthResponse signInAccount(LoginRequest request);

}
