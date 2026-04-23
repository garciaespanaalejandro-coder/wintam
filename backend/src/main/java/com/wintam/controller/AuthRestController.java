package com.wintam.controller;

import com.wintam.dto.AuthResponse;
import com.wintam.dto.LoginRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.dto.RegisterRequest;
import com.wintam.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createAccount(request));
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.signInAccount(loginRequest));
    }
}