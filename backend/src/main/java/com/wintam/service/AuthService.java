package com.wintam.service;

import com.wintam.dto.*;

public interface AuthService {
    MessageResponse createAccount(RegisterRequest request);
    AuthResponse signInAccount(LoginRequest request);
    AuthResponse verifyEmail(VerifiyRequest request);
    MessageResponse recoverPassword(RecoverRequest request);
    MessageResponse resetPassword(ResetPasswordRequest request);
}
